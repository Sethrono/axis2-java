package org.apache.axis.engine.deployment;

import junit.framework.TestCase;
import org.apache.axis.deployment.DeploymentEngine;
import org.apache.axis.deployment.DeploymentException;
import org.apache.axis.deployment.metadata.phaserule.PhaseException;
import org.apache.axis.engine.AxisFault;
import org.apache.axis.engine.registry.EngineRegistry;

/**
 * Copyright 2001-2004 The Apache Software Foundation.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Deepal Jayasinghe
 *         Nov 19, 2004
 *         4:47:32 PM
 *
 */
public class ServiceTest extends TestCase {

    public void testparseService1() throws PhaseException ,DeploymentException, AxisFault{
        String filename = "src/test-resources/deployment";
        DeploymentEngine deploymentEngine = new DeploymentEngine(filename);
        try {
            EngineRegistry er = null;
            try {
                er = deploymentEngine.start();
            } catch (AxisFault axisFault) {
                axisFault.printStackTrace();  //To change body of catch statement use Options | File Templates.
            } catch (PhaseException e) {
                e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            } catch (DeploymentException e) {
                e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            }
            if(er != null){
                System.out.println("Numbetr of service" + er.getServiceCount());
            }
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
    }
}
