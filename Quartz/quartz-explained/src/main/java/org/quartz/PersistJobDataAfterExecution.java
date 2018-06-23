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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that marks a {@link Job} class as one that makes updates to its
 * {@link JobDataMap} during execution, and wishes the scheduler to re-store the
 * <code>JobDataMap</code> when execution completes.
 *
 * 该注解表明：当scheduler执行该job的同时更新job关联的JobDataMap，job执行完之后，保存
 *  更新后的JobDataMap；则该job下次被执行的时候，使用的是更新后的JobDataMap；
 *   
 * <p>Jobs that are marked with this annotation should also seriously consider
 * using the {@link DisallowConcurrentExecution} annotation, to avoid data
 * storage race conditions with concurrently executing job instances.</p>
 *
 * 当使用该注解的时候，建议与DisallowConcurrentExecution注解同时使用，避免由于多线程
 *  同时执行该job导致JobDataMap数据的不一致；
 *
 * @see DisallowConcurrentExecution
 * 
 * @author jhouse
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PersistJobDataAfterExecution {

}
