/*
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved.
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

package org.quartz;

import java.io.Serializable;

/**
 * Conveys the detail properties of a given <code>Job</code> instance. JobDetails are
 * to be created/defined with {@link JobBuilder}.
 *
 * JobDetail是带有属性配置的Job实例；
 * JobDetail可以通过JobBuilder来创建；
 * 
 * <p>
 * Quartz does not store an actual instance of a <code>Job</code> class, but
 * instead allows you to define an instance of one, through the use of a <code>JobDetail</code>.
 * </p>
 *
 * quartz并不能直接定义Job实例，而是通过JobDetail来定义Job实例；
 * 
 * <p>
 * <code>Job</code>s have a name and group associated with them, which
 * should uniquely identify them within a single <code>{@link Scheduler}</code>.
 * </p>
 *
 * 每一个Job实例都有name和group这两个属性，他们确定了Job实例在一个scheduler中的唯一性；
 * 
 * <p>
 * <code>Trigger</code>s are the 'mechanism' by which <code>Job</code>s
 * are scheduled. Many <code>Trigger</code>s can point to the same <code>Job</code>,
 * but a single <code>Trigger</code> can only point to one <code>Job</code>.
 * </p>
 *
 * Job实例是由trigger触发的，一个job实例可以关联多个trigger，但一个trigger只能关联一个job实例；
 * 
 * @see JobBuilder
 * @see Job
 * @see JobDataMap
 * @see Trigger
 * 
 * @author James House
 */
public interface JobDetail extends Serializable, Cloneable {

    public JobKey getKey();

    /**
     * <p>
     * Return the description given to the <code>Job</code> instance by its
     * creator (if any).
     * </p>
     *
     * 返回Job实例的描述属性，如果没有设置则返回null；
     * 
     * @return null if no description was set.
     */
    public String getDescription();

    /**
     * <p>
     * Get the instance of <code>Job</code> that will be executed.
     * </p>
     *
     * 返回Job类的class名
     *
     */
    public Class<? extends Job> getJobClass();

    /**
     * <p>
     * Get the <code>JobDataMap</code> that is associated with the <code>Job</code>.
     * </p>
     *
     * 返回与该job实例关联的JobDataMap，即该job实例携带的数据；
     */
    public JobDataMap getJobDataMap();

    /**
     * <p>
     * Whether or not the <code>Job</code> should remain stored after it is
     * orphaned (no <code>{@link Trigger}s</code> point to it).
     * </p>
     *
     * 持续性，如果为true，则表示当没有trigger与该job实例关联的时候，删除该job实例；
     *  默认值为false；
     * 
     * <p>
     * If not explicitly set, the default value is <code>false</code>.
     * </p>
     * 
     * @return <code>true</code> if the Job should remain persisted after
     *         being orphaned.
     */
    public boolean isDurable();

    /**
     * @see PersistJobDataAfterExecution
     *
     * 在job执行后，job的数据是否需要持久化，参考{@link PersistJobDataAfterExecution}
     *
     * @return whether the associated Job class carries the {@link PersistJobDataAfterExecution} annotation.
     */
    public boolean isPersistJobDataAfterExecution();

    /**
     * @see DisallowConcurrentExecution
     *
     * 该job是否支持并发执行
     *
     * @return whether the associated Job class carries the {@link DisallowConcurrentExecution} annotation.
     */
    public boolean isConcurrentExectionDisallowed();

    /**
     * <p>
     * Instructs the <code>Scheduler</code> whether or not the <code>Job</code>
     * should be re-executed if a 'recovery' or 'fail-over' situation is
     * encountered.
     * </p>
     *
     * 设置该JobDetail的可恢复性，即当系统从故障中恢复后，该JobDetail是否被scheduler
     *  重新调度执行；默认值为false；
     * 
     * <p>
     * If not explicitly set, the default value is <code>false</code>.
     * </p>
     * 
     * @see JobExecutionContext#isRecovering()
     */
    public boolean requestsRecovery();

    public Object clone();
    
    /**
     * Get a {@link JobBuilder} that is configured to produce a 
     * <code>JobDetail</code> identical to this one.
     *
     * 返回可以创建当前JobDetail实例的JobBuilder
     *
     */
    public JobBuilder getJobBuilder();

}