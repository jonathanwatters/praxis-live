/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2012 Neil C Smith.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details.
 *
 * You should have received a copy of the GNU General Public License version 3
 * along with this work; if not, see http://www.gnu.org/licenses/
 *
 *
 * Please visit http://neilcsmith.net if you need additional information or
 * have any questions.
 */
package org.praxislive.ide.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.praxislive.core.Component;
import org.praxislive.core.services.ComponentFactory;
import org.praxislive.ide.core.api.ExtensionProvider;
import org.praxislive.ide.core.api.LogHandler;
import org.praxislive.ide.core.api.RootLifecycleHandler;
import org.praxislive.ide.core.api.Task;
import org.praxislive.logging.LogLevel;
import org.openide.util.Lookup;

/**
 *
 * @author Neil C Smith <http://neilcsmith.net>
 */
class Utils {
    
    
    private Utils() {}
    
    static List<Task> findRootDeletionTasks(String description, Set<String> roots) {
        List<Task> tasks = new ArrayList<Task>();
        for (RootLifecycleHandler handler :
                Lookup.getDefault().lookupAll(RootLifecycleHandler.class)) {
            Task task = handler.getDeletionTask(description, roots);
            if (task != null) {
                tasks.add(task);
            }
        }
        return tasks;
    }
    
    static Component[] findExtensions() {
        Collection<? extends ExtensionProvider> providers =
                Lookup.getDefault().lookupAll(ExtensionProvider.class);
        List<Component> list = new ArrayList<Component>(providers.size());
        for (ExtensionProvider provider : providers) {
            list.add(provider.getExtensionComponent());
        }
        return list.toArray(new Component[list.size()]);
    }
    
    static ComponentFactory findCoreFactory() {
        CoreFactoryProvider provider = Lookup.getDefault().lookup(CoreFactoryProvider.class);
        return provider == null ? null : provider.getFactory();
    }
    
    static List<LogHandler> findLogHandlers() {
        return new ArrayList<>(Lookup.getDefault().lookupAll(LogHandler.class));
    }
    
    static LogLevel findLogLevel(List<LogHandler> handlers) {
        LogLevel level = LogLevel.ERROR;
        for (LogHandler handler : handlers) {
            LogLevel l = handler.getLevel();
            if (!level.isLoggable(l)) {
                level = l;
            }
        }
        return level;
    }
    
}
