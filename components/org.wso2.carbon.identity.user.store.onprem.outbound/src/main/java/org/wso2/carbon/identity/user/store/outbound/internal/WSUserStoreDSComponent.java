/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.identity.user.store.outbound.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.identity.user.store.outbound.WSOutboundUserStoreManager;
import org.wso2.carbon.identity.user.store.outbound.util.DatabaseUtil;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.user.api.UserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * @scr.component name="agent.outbound.ws.user.store.component" immediate=true
 * @scr.reference name="user.realmservice.default"
 * interface="org.wso2.carbon.user.core.service.RealmService"
 * cardinality="1..1" policy="dynamic" bind="setRealmService"
 * unbind="unsetRealmService"
 * @scr.reference name="registry.service"
 * interface="org.wso2.carbon.registry.core.service.RegistryService"
 * cardinality="1..1" policy="dynamic" bind="setRegistryService"
 * unbind="unsetRegistryService"
 */
public class WSUserStoreDSComponent {

    private static Log log = LogFactory.getLog(WSUserStoreDSComponent.class);

    protected void activate(ComponentContext ctxt) {
        try {

            UserStoreManager remoteStoreManager = new WSOutboundUserStoreManager();
            DatabaseUtil.getInstance();
            ctxt.getBundleContext().registerService(UserStoreManager.class.getName(),
                    remoteStoreManager, null);
            if (log.isDebugEnabled()) {
                log.debug("Cloud outbound user store manager activated successfully.");
            }

        } catch (Throwable e) {
            log.error("Failed to activate Cloud outbound user store manager.", e);
        }
    }

    protected void deactivate(ComponentContext ctxt) {
        if (log.isDebugEnabled()) {
            log.debug("Carbon Carbon Remote User Store is deactivated ");
        }
    }

    protected void setRealmService(RealmService realmService) {
        WSUserStoreComponentHolder.getInstance().setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {
        WSUserStoreComponentHolder.getInstance().setRealmService(null);
    }

    public static void setRegistryService(RegistryService registryService) {
        WSUserStoreComponentHolder.getInstance().setRegistryService(registryService);
    }

    public static RegistryService getRegistryService() {
        return WSUserStoreComponentHolder.getInstance().getRegistryService();
    }

    protected void unsetRegistryService(RegistryService registryService) {
        if (log.isDebugEnabled()) {
            log.debug("RegistryService unset in user Store bundle");
        }
        WSUserStoreComponentHolder.getInstance().setRegistryService(null);
    }

}
