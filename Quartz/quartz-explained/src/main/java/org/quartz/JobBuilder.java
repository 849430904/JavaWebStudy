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

import org.quartz.impl.JobDetailImpl;
import org.quartz.utils.Key;

/**
 * <code>JobBuilder</code> is used to instantiate {@link JobDetail}s.
 *
 * <p>The builder will always try to keep itself in a valid state, with 
 * reasonable defaults set for calling build() at any point.  For instance
 * if you do not invoke <i>withIdentity(..)</i> a job name will be generated
 * for you.</p>
 *
 * JobBuilder用于创建JobDetail实例；无论何时调用其build()方法创建实例，相关的属性
 *  参数都有合理的默认值；比如，如果没有调用withIdentity()方法设置name和group属性，
 *  则它会为JobDetail自动生成一个name属性，group属性使用默认值null；
 *   
 * <p>Quartz provides a builder-style API for constructing scheduling-related
 * entities via a Domain-Specific Language (DSL).  The DSL can best be
 * utilized through the usage of static imports of the methods on the classes
 * <code>TriggerBuilder</code>, <code>JobBuilder</code>, 
 * <code>DateBuilder</code>, <code>JobKey</code>, <code>TriggerKey</code> 
 * and the various <code>ScheduleBuilder</code> implementations.</p>
 *
 * quartz使用builder模式的API构建调度相关的对象，只需要静态导入TriggerBuilder、
 *  JobBuilder、DateBuilder、JobKey、TriggerKey等类的静态方法即可；
 * 
 * <p>Client code can then use the DSL to write code such as this:</p>
 * <pre>
 *         JobDetail job = newJob(MyJob.class)
 *             .withIdentity("myJob")
 *             .build();
 *             
 *         Trigger trigger = newTrigger() 
 *             .withIdentity(triggerKey("myTrigger", "myTriggerGroup"))
 *             .withSchedule(simpleSchedule()
 *                 .withIntervalInHours(1)
 *                 .repeatForever())
 *             .startAt(futureDate(10, MINUTES))
 *             .build();
 *         
 *         scheduler.scheduleJob(job, trigger);
 * <pre>
 *  
 * @see TriggerBuilder
 * @see DateBuilder 
 * @see JobDetail
 */
public class JobBuilder {

    private JobKey key;
    private String description;
    private Class<? extends Job> jobClass;
    private boolean durability;
    private boolean shouldRecover;
    
    private JobDataMap jobDataMap = new JobDataMap();

    /**
     * 构造函数不是public的，禁止通过new创建对象；
     */
    protected JobBuilder() {
    }
    
    /**
     * Create a JobBuilder with which to define a <code>JobDetail</code>.
     *
     * 通过静态方法返回一个JobBuilder的实例；
     *
     * @return a new JobBuilder
     */
    public static JobBuilder newJob() {
        return new JobBuilder();
    }
    
    /**
     * Create a JobBuilder with which to define a <code>JobDetail</code>,
     * and set the class name of the <code>Job</code> to be executed.
     *
     * 通过job的class名创建JobBuilder的实例；
     * 
     * @return a new JobBuilder
     */
    public static JobBuilder newJob(Class <? extends Job> jobClass) {
        JobBuilder b = new JobBuilder();
        b.ofType(jobClass);
        return b;
    }

    /**
     * Produce the <code>JobDetail</code> instance defined by this 
     * <code>JobBuilder</code>.
     *
     * 创建JobDetail实例，如果属性没有设置，则使用合理的默认值；
     * 
     * @return the defined JobDetail.
     */
    public JobDetail build() {

        JobDetailImpl job = new JobDetailImpl();
        
        job.setJobClass(jobClass);
        job.setDescription(description);
        if(key == null)
            key = new JobKey(Key.createUniqueName(null), null);
        job.setKey(key); 
        job.setDurability(durability);
        job.setRequestsRecovery(shouldRecover);
        
        
        if(!jobDataMap.isEmpty())
            job.setJobDataMap(jobDataMap);
        
        return job;
    }
    
    /**
     * Use a <code>JobKey</code> with the given name and default group to
     * identify the JobDetail.
     * 
     * <p>If none of the 'withIdentity' methods are set on the JobBuilder,
     * then a random, unique JobKey will be generated.</p>
     *
     * 使用参数name作为JobKey的名称，group使用默认值，构造一个JobKey对象；
     * 如果没有调用该方法，则在build()时，会随机生成一个唯一的name；
     *
     * 
     * @param name the name element for the Job's JobKey
     * @return the updated JobBuilder
     * @see JobKey
     * @see JobDetail#getKey()
     */
    public JobBuilder withIdentity(String name) {
        key = new JobKey(name, null);
        return this;
    }  
    
    /**
     * Use a <code>JobKey</code> with the given name and group to
     * identify the JobDetail.
     * 
     * <p>If none of the 'withIdentity' methods are set on the JobBuilder,
     * then a random, unique JobKey will be generated.</p>
     *
     * 使用name和group参数构造一个JobKey对象；
     * 
     * @param name the name element for the Job's JobKey
     * @param group the group element for the Job's JobKey
     * @return the updated JobBuilder
     * @see JobKey
     * @see JobDetail#getKey()
     */
    public JobBuilder withIdentity(String name, String group) {
        key = new JobKey(name, group);
        return this;
    }
    
    /**
     * Use a <code>JobKey</code> to identify the JobDetail.
     * 
     * <p>If none of the 'withIdentity' methods are set on the JobBuilder,
     * then a random, unique JobKey will be generated.</p>
     *
     * 使用一个JobKey对象来设置当前的jobKey；
     * 
     * @param jobKey the Job's JobKey
     * @return the updated JobBuilder
     * @see JobKey
     * @see JobDetail#getKey()
     */
    public JobBuilder withIdentity(JobKey jobKey) {
        this.key = jobKey;
        return this;
    }
    
    /**
     * Set the given (human-meaningful) description of the Job.
     *
     * 设置job的描述；
     * 
     * @param jobDescription the description for the Job
     * @return the updated JobBuilder
     * @see JobDetail#getDescription()
     */
    public JobBuilder withDescription(String jobDescription) {
        this.description = jobDescription;
        return this;
    }
    
    /**
     * Set the class which will be instantiated and executed when a
     * Trigger fires that is associated with this JobDetail.
     *
     * 设置这个JobDetail所代表的Job；
     *
     * @param jobClazz a class implementing the Job interface.
     * @return the updated JobBuilder
     * @see JobDetail#getJobClass()
     */
    public JobBuilder ofType(Class <? extends Job> jobClazz) {
        this.jobClass = jobClazz;
        return this;
    }

    /**
     * Instructs the <code>Scheduler</code> whether or not the <code>Job</code>
     * should be re-executed if a 'recovery' or 'fail-over' situation is
     * encountered.
     * 
     * <p>
     * If not explicitly set, the default value is <code>false</code>.
     * </p>
     *
     * 故障恢复后，该JobDetail是否需要重新调度执行；默认为false；
     * 
     * @return the updated JobBuilder
     * @see JobDetail#requestsRecovery()
     */
    public JobBuilder requestRecovery() {
        this.shouldRecover = true;
        return this;
    }

    /**
     * Instructs the <code>Scheduler</code> whether or not the <code>Job</code>
     * should be re-executed if a 'recovery' or 'fail-over' situation is
     * encountered.
     * 
     * <p>
     * If not explicitly set, the default value is <code>false</code>.
     * </p>
     *
     * 故障恢复后，该JobDetail是否需要重新调度执行；默认为false；
     * 
     * @param jobShouldRecover the desired setting
     * @return the updated JobBuilder
     */
    public JobBuilder requestRecovery(boolean jobShouldRecover) {
        this.shouldRecover = jobShouldRecover;
        return this;
    }

    /**
     * Whether or not the <code>Job</code> should remain stored after it is
     * orphaned (no <code>{@link Trigger}s</code> point to it).
     * 
     * <p>
     * If not explicitly set, the default value is <code>false</code> 
     * - this method sets the value to <code>true</code>.
     * </p>
     *
     * 如果该JobDetail没有trigger相关联时，是否保存；默认为false，即不保存；
     * 
     * @return the updated JobBuilder
     * @see JobDetail#isDurable()
     */
    public JobBuilder storeDurably() {
        this.durability = true;
        return this;
    }
    
    /**
     * Whether or not the <code>Job</code> should remain stored after it is
     * orphaned (no <code>{@link Trigger}s</code> point to it).
     * 
     * <p>
     * If not explicitly set, the default value is <code>false</code>.
     * </p>
     *
     * 如果该JobDetail没有trigger相关联时，是否保存；默认为false，即不保存；
     * 
     * @param jobDurability the value to set for the durability property.
     * @return the updated JobBuilder
     * @see JobDetail#isDurable()
     */
    public JobBuilder storeDurably(boolean jobDurability) {
        this.durability = jobDurability;
        return this;
    }
    
    /**
     * Add the given key-value pair to the JobDetail's {@link JobDataMap}.
     *
     * JobDetail的数据，key/value形式的map；该方法表示在已有的数据上增加一个
     * key/value对；
     * 
     * @return the updated JobBuilder
     * @see JobDetail#getJobDataMap()
     */
    public JobBuilder usingJobData(String dataKey, String value) {
        jobDataMap.put(dataKey, value);
        return this;
    }
    
    /**
     * Add the given key-value pair to the JobDetail's {@link JobDataMap}.
     * 
     * @return the updated JobBuilder
     * @see JobDetail#getJobDataMap()
     */
    public JobBuilder usingJobData(String dataKey, Integer value) {
        jobDataMap.put(dataKey, value);
        return this;
    }
    
    /**
     * Add the given key-value pair to the JobDetail's {@link JobDataMap}.
     * 
     * @return the updated JobBuilder
     * @see JobDetail#getJobDataMap()
     */
    public JobBuilder usingJobData(String dataKey, Long value) {
        jobDataMap.put(dataKey, value);
        return this;
    }
    
    /**
     * Add the given key-value pair to the JobDetail's {@link JobDataMap}.
     * 
     * @return the updated JobBuilder
     * @see JobDetail#getJobDataMap()
     */
    public JobBuilder usingJobData(String dataKey, Float value) {
        jobDataMap.put(dataKey, value);
        return this;
    }
    
    /**
     * Add the given key-value pair to the JobDetail's {@link JobDataMap}.
     * 
     * @return the updated JobBuilder
     * @see JobDetail#getJobDataMap()
     */
    public JobBuilder usingJobData(String dataKey, Double value) {
        jobDataMap.put(dataKey, value);
        return this;
    }
    
    /**
     * Add the given key-value pair to the JobDetail's {@link JobDataMap}.
     * 
     * @return the updated JobBuilder
     * @see JobDetail#getJobDataMap()
     */
    public JobBuilder usingJobData(String dataKey, Boolean value) {
        jobDataMap.put(dataKey, value);
        return this;
    }
    
    /**
     * Add all the data from the given {@link JobDataMap} to the
     * {@code JobDetail}'s {@code JobDataMap}.
     *
     * 将参数newJobDataMap中的数据都添加到已有的jobDataMap中；
     * 
     * @return the updated JobBuilder
     * @see JobDetail#getJobDataMap()
     */
    public JobBuilder usingJobData(JobDataMap newJobDataMap) {
        jobDataMap.putAll(newJobDataMap);
        return this;
    }

    /**
     * Replace the {@code JobDetail}'s {@link JobDataMap} with the
     * given {@code JobDataMap}.
     *
     * 使用参数newJobDataMap替换已有的jobDataMap；
     * 
     * @return the updated JobBuilder
     * @see JobDetail#getJobDataMap() 
     */
    public JobBuilder setJobData(JobDataMap newJobDataMap) {
        jobDataMap = newJobDataMap;
        return this;
    }
}
