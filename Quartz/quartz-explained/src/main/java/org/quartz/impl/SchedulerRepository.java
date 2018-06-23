/* 
 * Copyright 2001-2009 Terracotta, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

package org.quartz.impl;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import java.util.Collection;
import java.util.HashMap;

/**
 * <p>
 * Holds references to Scheduler instances - ensuring uniqueness, and
 * preventing garbage collection, and allowing 'global' lookups - all within a
 * ClassLoader space.
 * </p>
 *
 * 实例仓库：保存所有已创建的scheduler实例；内部通过HashMap实现，key为scheduler名称，
 *  value为scheduler实例：确保全局的唯一性，防止被垃圾回收，同时用于全局的查找；
 * 
 * @author James House
 */
public class SchedulerRepository {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private HashMap<String, Scheduler> schedulers;

    private static SchedulerRepository inst;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     *
     * 私有的构造函数，无法从外部创建实例；
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private SchedulerRepository() {
        schedulers = new HashMap<String, Scheduler>();
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     *
     * 单例模式
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static synchronized SchedulerRepository getInstance() {
        if (inst == null) {
            inst = new SchedulerRepository();
        }

        return inst;
    }

    /**
     * 将scheduler添加到HashMap中，如果该scheduler在HashMap中已存在，抛异常；
     *
     * @param sched
     * @throws SchedulerException
     */
    public synchronized void bind(Scheduler sched) throws SchedulerException {

        if ((Scheduler) schedulers.get(sched.getSchedulerName()) != null) {
            throw new SchedulerException("Scheduler with name '"
                    + sched.getSchedulerName() + "' already exists.");
        }

        schedulers.put(sched.getSchedulerName(), sched);
    }

    public synchronized boolean remove(String schedName) {
        return (schedulers.remove(schedName) != null);
    }

    /**
     * 通过scheduler的名称查询对应的scheduler实例；
     *
     * @param schedName
     * @return
     */
    public synchronized Scheduler lookup(String schedName) {
        return schedulers.get(schedName);
    }

    /**
     * 返回所有的Scheduler，返回结果不可更改
     *
     * @return
     */
    public synchronized Collection<Scheduler> lookupAll() {
        return java.util.Collections
                .unmodifiableCollection(schedulers.values());
    }

}