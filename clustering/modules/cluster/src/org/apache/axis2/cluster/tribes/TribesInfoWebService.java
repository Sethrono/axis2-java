package org.apache.axis2.cluster.tribes;

import java.util.List;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.catalina.tribes.Member;

public class TribesInfoWebService {

	private ServiceContext srvCtx;
	
	public void init(ServiceContext context){
	   this.srvCtx = context;	
	}
	
	public OMElement getMembershipInfo(){
		ConfigurationContext configCtx = srvCtx.getConfigurationContext();
		TransientTribesMemberInfo memberInfo = (TransientTribesMemberInfo)configCtx.getProperty("MEMBER_INFO");
		
		List <Member> liveMembers = memberInfo.getLiveNodes();
		List <Member> deadMembers = memberInfo.getDeadNodes();
		
		SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
        OMNamespace omNs = factory.createOMNamespace("http://org.apache.axis2.cluster.tribes/xsd", "tribes");
        
        OMElement memberInfoOM = factory.createOMElement("memberInfo",omNs);
        
        OMElement liveMemberOM = factory.createOMElement("liveMemberInfo",omNs);
        for (Member member : liveMembers){
        	OMElement memberOM = factory.createOMElement("member",omNs);
        	memberOM.addAttribute(factory.createOMAttribute("name", omNs, member.getName()));
        	memberOM.addAttribute(factory.createOMAttribute("host", omNs, String.valueOf(member.getHost())));
        	memberOM.addAttribute(factory.createOMAttribute("port", omNs, String.valueOf(member.getPort())));
        	memberOM.addAttribute(factory.createOMAttribute("aliveTime", omNs, String.valueOf(member.getMemberAliveTime())));
        	liveMemberOM.addChild(memberOM);
        }
        
        OMElement deadMemberOM = factory.createOMElement("deadMemberInfo",omNs);
        for (Member member : deadMembers){
        	OMElement memberOM = factory.createOMElement("member",omNs);
        	memberOM.addAttribute(factory.createOMAttribute("name", omNs, member.getName()));
        	memberOM.addAttribute(factory.createOMAttribute("host", omNs, String.valueOf(member.getHost())));
        	memberOM.addAttribute(factory.createOMAttribute("port", omNs, String.valueOf(member.getPort())));
        	memberOM.addAttribute(factory.createOMAttribute("aliveTime", omNs, String.valueOf(member.getMemberAliveTime())));
        	deadMemberOM.addChild(memberOM);
        }
        
        memberInfoOM.addChild(liveMemberOM);
        memberInfoOM.addChild(deadMemberOM);
        
        return memberInfoOM;
	}
	
	public OMElement getChannelInfo(){
		ConfigurationContext configCtx = srvCtx.getConfigurationContext();
		TransientTribesChannelInfo channelInfo = (TransientTribesChannelInfo)configCtx.getProperty("CHANNEL_INFO");
		
		SOAPFactory factory = OMAbstractFactory.getSOAP11Factory();
        OMNamespace omNs = factory.createOMNamespace("http://org.apache.axis2.cluster.tribes/xsd", "tribes");
        OMElement channelInfoOM = factory.createOMElement("channelInfo",omNs);
        
        OMElement messageCountOM = factory.createOMElement("messageCount",omNs);
        messageCountOM.addChild(factory.createOMText(messageCountOM, String.valueOf(channelInfo.getMessageCount())));
        
        OMElement grpCtxCreationCountOM = factory.createOMElement("grpCtxCreationCount",omNs);
        grpCtxCreationCountOM.addChild(factory.createOMText(grpCtxCreationCountOM, String.valueOf(channelInfo.getGrpCtxCreationCount())));
        
        OMElement grpCtxRemoveCountOM = factory.createOMElement("grpCtxRemoveCount",omNs);
        grpCtxRemoveCountOM.addChild(factory.createOMText(grpCtxRemoveCountOM, String.valueOf(channelInfo.getGrpCtxRemoveCount())));
        
        OMElement srvCtxCreationCountOM = factory.createOMElement("srvCtxCreationCount",omNs);
        srvCtxCreationCountOM.addChild(factory.createOMText(srvCtxCreationCountOM, String.valueOf(channelInfo.getSrvCtxCreationCount())));
        
        OMElement srvCtxRemoveCountOM = factory.createOMElement("srvCtxRemoveCount",omNs);
        srvCtxRemoveCountOM.addChild(factory.createOMText(srvCtxRemoveCountOM, String.valueOf(channelInfo.getSrvCtxRemoveCount())));
        
        OMElement updateStateCountOM = factory.createOMElement("updateStateCount",omNs);
        updateStateCountOM.addChild(factory.createOMText(updateStateCountOM, String.valueOf(channelInfo.getUpdateStateCount())));
        
        channelInfoOM.addChild(messageCountOM);
        channelInfoOM.addChild(grpCtxCreationCountOM);
        channelInfoOM.addChild(grpCtxRemoveCountOM);
        channelInfoOM.addChild(srvCtxCreationCountOM);
        channelInfoOM.addChild(srvCtxRemoveCountOM);
        channelInfoOM.addChild(updateStateCountOM);
        
        return channelInfoOM;
	}	
}