/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
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
package org.apache.axis2.jaxws.client.dispatch;

import org.apache.axis2.jaxws.client.TestClientInvocationController;
import org.apache.axis2.jaxws.client.TestClientInvocationControllerFactory;
import org.apache.axis2.jaxws.core.InvocationContext;
import org.apache.axis2.jaxws.core.MessageContext;
import org.apache.axis2.jaxws.core.controller.InvocationControllerFactory;
import org.apache.axis2.jaxws.registry.FactoryRegistry;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.ws.soap.SOAPBinding;

import junit.framework.TestCase;

/**
 * This suite of tests is for the MTOMFeature configuration that can
 * be used on Dispatch clients.
 */
public class DispatchMTOMFeatureTest extends TestCase {

    private InvocationControllerFactory oldFactory;
    private TestClientInvocationControllerFactory newFactory;
    private TestClientInvocationController testController;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        InvocationControllerFactory icf = (InvocationControllerFactory) FactoryRegistry.getFactory(InvocationControllerFactory.class);
        oldFactory = icf;
        
        testController = new TestClientInvocationController();
        
        newFactory = new TestClientInvocationControllerFactory();
        newFactory.setInvocationController(testController);
        
        FactoryRegistry.setFactory(InvocationControllerFactory.class, newFactory);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        FactoryRegistry.setFactory(InvocationControllerFactory.class, oldFactory);
    }

    /*
     * Make sure MTOM is not enabled by default.
     */
    public void testNoMTOMFeature() {
        Service svc = Service.create(new QName("http://test", "TestService"));
        svc.addPort(new QName("http://test", "TestPort"), SOAPBinding.SOAP11HTTP_BINDING, "http://localhost");
        
        Dispatch<Source> d = svc.createDispatch(new QName("http://test", "TestPort"), Source.class, Service.Mode.PAYLOAD);
        
        d.invoke(null);
        
        InvocationContext ic = testController.getInvocationContext();
        MessageContext request = ic.getRequestMessageContext();
        
        assertFalse("MTOM should not be enabled by default.", request.getMessage().isMTOMEnabled());
    }
    
    /*
     * Test the default configuration of the MTOMFeature.
     */
    public void testDefaultMTOMFeature() {
        Service svc = Service.create(new QName("http://test", "TestService"));
        svc.addPort(new QName("http://test", "TestPort"), SOAPBinding.SOAP11HTTP_BINDING, "http://localhost");
        
        // Use the default feature config
        MTOMFeature feature = new MTOMFeature();
        
        Dispatch<Source> d = svc.createDispatch(new QName("http://test", "TestPort"), 
            Source.class, Service.Mode.PAYLOAD, feature);
        
        d.invoke(null);
        
        InvocationContext ic = testController.getInvocationContext();
        MessageContext request = ic.getRequestMessageContext();
        
        assertTrue("MTOM should be enabled via the MTOMFeature.", request.getMessage().isMTOMEnabled());
    }
    
    /*
     * Test disabling the MTOM feature.
     */
    public void testDisabledMTOMFeature() {
        Service svc = Service.create(new QName("http://test", "TestService"));
        svc.addPort(new QName("http://test", "TestPort"), SOAPBinding.SOAP11HTTP_BINDING, "http://localhost");
        
        // Set the feature to be disabled.
        MTOMFeature feature = new MTOMFeature(false);
                
        Dispatch<Source> d = svc.createDispatch(new QName("http://test", "TestPort"), 
            Source.class, Service.Mode.PAYLOAD, feature);
        
        d.invoke(null);
        
        InvocationContext ic = testController.getInvocationContext();
        MessageContext request = ic.getRequestMessageContext();
        
        assertFalse("MTOM should be disabled via the MTOMFeature.", request.getMessage().isMTOMEnabled());
    }
    
    /*
     * Test the configuration of the threshold for MTOM.
     */
    public void testMTOMFeatureThreshold() {
        Service svc = Service.create(new QName("http://test", "TestService"));
        svc.addPort(new QName("http://test", "TestPort"), SOAPBinding.SOAP11HTTP_BINDING, "http://localhost");
        
        // Set the feature to be disabled.
        int threshold = 20000;
        MTOMFeature feature = new MTOMFeature(threshold);
                
        Dispatch<Source> d = svc.createDispatch(new QName("http://test", "TestPort"), 
            Source.class, Service.Mode.PAYLOAD, feature);
        
        d.invoke(null);
        
        InvocationContext ic = testController.getInvocationContext();
        MessageContext request = ic.getRequestMessageContext();
        
        assertFalse("MTOM should not be enabled.  The threshold was not exceeded.", request.getMessage().isMTOMEnabled());
    }
    
    /*
     * Test the co-existence of an MTOMFeature and a MTOM binding type for a client.
     */
    public void testMTOMFeatureAndBinding() {
        Service svc = Service.create(new QName("http://test", "TestService"));
        svc.addPort(new QName("http://test", "TestPort"), SOAPBinding.SOAP11HTTP_MTOM_BINDING, "http://localhost");
        
        // Use the default feature config
        MTOMFeature feature = new MTOMFeature();
        
        Dispatch<Source> d = svc.createDispatch(new QName("http://test", "TestPort"), 
            Source.class, Service.Mode.PAYLOAD, feature);
        
        d.invoke(null);
        
        InvocationContext ic = testController.getInvocationContext();
        MessageContext request = ic.getRequestMessageContext();
        
        assertTrue("MTOM should be enabled via the MTOMFeature.", request.getMessage().isMTOMEnabled());
    }
    
    /*
     * Test the override of an MTOM binding by disabling MTOM via the MTOMFeature.
     */
    public void testMTOMFeatureAndBindingOverride() {
        Service svc = Service.create(new QName("http://test", "TestService"));
        svc.addPort(new QName("http://test", "TestPort"), SOAPBinding.SOAP11HTTP_MTOM_BINDING, "http://localhost");
        
        // Use the default feature config
        MTOMFeature feature = new MTOMFeature(false);
        
        Dispatch<Source> d = svc.createDispatch(new QName("http://test", "TestPort"), 
            Source.class, Service.Mode.PAYLOAD, feature);
        
        d.invoke(null);
        
        InvocationContext ic = testController.getInvocationContext();
        MessageContext request = ic.getRequestMessageContext();
        
        assertFalse("MTOM should be disabled via the MTOMFeature.", request.getMessage().isMTOMEnabled());        
    }
}
