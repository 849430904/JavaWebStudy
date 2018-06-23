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
 * An annotation that marks a {@link Job} class as one that must not have multiple
 * instances executed concurrently (where instance is based-upon a {@link JobDetail} 
 * definition - or in other words based upon a {@link JobKey}).
 *
 * 这个注解表示不能在同一时刻执行同一个JobDetail的多个实例；这个注解是用在Job上的，但是
 *  这个并发性的限制是针对JobDetail的。
 *
 * 为了帮助理解，举个简单的例子：加入我们定义了一个job，叫CacheJob，并在该job上使用本注解，
 *  然后我们定义了两个JobDetail实例，分别为MemcachedJobDetail和RedisJobDetail，这个注解
 *  的限制就是：我们不能同时执行多个MemcachedJobDetail的实例，但是我们在执行MemcachedJobDetail
 *  实例时，是可以同时执行RedisJobDetail实例的。
 *
 * @see PersistJobDataAfterExecution
 * 
 * @author jhouse
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DisallowConcurrentExecution {

}
