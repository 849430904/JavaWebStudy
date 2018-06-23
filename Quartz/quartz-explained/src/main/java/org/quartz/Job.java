
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

package org.quartz;

/**
 * <p>
 * The interface to be implemented by classes which represent a 'job' to be
 * performed.
 * </p>
 * 
 * <p>
 * Instances of <code>Job</code> must have a <code>public</code>
 * no-argument constructor.
 * </p>
 * 
 * <p>
 * <code>JobDataMap</code> provides a mechanism for 'instance member data'
 * that may be required by some implementations of this interface.
 * </p>
 *
 * Job接口，自定义的job需要实现这个接口，且保留默认的构造函数(public，不带参数)；
 * 
 * @see JobDetail
 * @see JobBuilder
 * @see ExecuteInJTATransaction
 * @see DisallowConcurrentExecution
 * @see PersistJobDataAfterExecution
 * @see Trigger
 * @see Scheduler
 * 
 * @author James House
 */
public interface Job {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Called by the <code>{@link Scheduler}</code> when a <code>{@link Trigger}</code>
     * fires that is associated with the <code>Job</code>.
     * </p>
     *
     * Job是在相关联的trigger被触发时，由scheduler调用执行；
     * 
     * <p>
     * The implementation may wish to set a 
     * {@link JobExecutionContext#setResult(Object) result} object on the 
     * {@link JobExecutionContext} before this method exits.  The result itself
     * is meaningless to Quartz, but may be informative to 
     * <code>{@link JobListener}s</code> or 
     * <code>{@link TriggerListener}s</code> that are watching the job's 
     * execution.
     * </p>
     *
     * Job的实现类，可以在execute()退出之前，调用context对象的setResult(Object)方法设置一个对象值，
     * 这个result对于quartz是没有意义的，但是可以在该job的JobListener和TriggerListener中通过调用
     * context对象的getResult()方法获取该result值。
     * 
     * @throws JobExecutionException
     *           if there is an exception while executing the job.
     */
    void execute(JobExecutionContext context)
        throws JobExecutionException;

}
