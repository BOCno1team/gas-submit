package main.java.org.example.cfc;

import java.io.File;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;

import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import main.java.org.example.user.UserContext;
import main.java.org.example.util.Util;

public class InvokeBCP {
	private static UserContext adminUserContext = null;
	private static CryptoSuite cryptoSuite = null;
	private static HFClient hfClient = null;
	private static Channel channel = null;
	private static Channel initChannel() throws Exception{
		adminUserContext = Util.readUserContext("org1", "ca");
		cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
		hfClient = HFClient.createNewInstance();
		hfClient.setCryptoSuite(cryptoSuite);
		hfClient.setUserContext(adminUserContext);
		if(null != channel) 
			return channel;
		String peer_name = "Peer Org1";
		String peer_url = "grpcs://184.172.214.135:30603"; // Ensure that port is of peer1
		//String pemStr = "-----BEGIN CERTIFICATE-----\nMIICITCCAcigAwIBAgIUKa3+XMvoLwc7wxP0nZDr+re8rgswCgYIKoZIzj0EAwIw\nYjELMAkGA1UEBhMCVVMxFzAVBgNVBAgTDk5vcnRoIENhcm9saW5hMRQwEgYDVQQK\nEwtIeXBlcmxlZGdlcjEPMA0GA1UECxMGRmFicmljMRMwEQYDVQQDEwpPcmcxQ0Et\ndGxzMB4XDTE5MDYyNTA1MzcwMFoXDTM0MDYyMTA1MzcwMFowYjELMAkGA1UEBhMC\nVVMxFzAVBgNVBAgTDk5vcnRoIENhcm9saW5hMRQwEgYDVQQKEwtIeXBlcmxlZGdl\ncjEPMA0GA1UECxMGRmFicmljMRMwEQYDVQQDEwpPcmcxQ0EtdGxzMFkwEwYHKoZI\nzj0CAQYIKoZIzj0DAQcDQgAE5vo/s6vZyAsAVf6j5Wrl/wGVGdvlVgZ0OfvuJQBx\nuHxd82c9YqT71YGgZhn0X+xIKGvx3DT21Q4x9RUR+H6+5qNcMFowDgYDVR0PAQH/\nBAQDAgEGMBIGA1UdEwEB/wQIMAYBAf8CAQEwHQYDVR0OBBYEFAO34fp2LeT/74yu\nwymuI51GrKPCMBUGA1UdEQQOMAyHBLis1oeHBApMwakwCgYIKoZIzj0EAwIDRwAw\nRAIgRY2fyDj6TgQj3UFrP2EVoSl44fQxw6qOIV5bbLJsgsYCIBXZ3PGbrcVODiP5\n564q4uwmyHk+jk/s+AgBboBSmTCK\n-----END CERTIFICATE-----";

		String peercert = "pems\\peer.pem"; 
	    File peerpemfile = new File(peercert); 			
		
		Properties peer_properties = new Properties();
		peer_properties.setProperty("pemFile", peerpemfile.getAbsolutePath());
		 //peer_properties.put("pemBytes", pemStr.getBytes());
		peer_properties.setProperty("sslProvider", "openSSL");
		peer_properties.setProperty("negotiationType", "TLS");
		//peer_properties.setProperty("hostnameOverride", "184.172.214.135");
		Peer peer = hfClient.newPeer(peer_name, peer_url, peer_properties);
//////////////////////////////////////order start 
		String orderer_name = "ordermsp";
		String orderer_url = "grpcs://184.172.214.135:32277"; // ensure that port is of orderer
		 //String pemStr1 = "-----BEGIN CERTIFICATE-----\nMIICITCCAcigAwIBAgIUKa3+XMvoLwc7wxP0nZDr+re8rgswCgYIKoZIzj0EAwIw\nYjELMAkGA1UEBhMCVVMxFzAVBgNVBAgTDk5vcnRoIENhcm9saW5hMRQwEgYDVQQK\nEwtIeXBlcmxlZGdlcjEPMA0GA1UECxMGRmFicmljMRMwEQYDVQQDEwpPcmcxQ0Et\ndGxzMB4XDTE5MDYyNTA1MzcwMFoXDTM0MDYyMTA1MzcwMFowYjELMAkGA1UEBhMC\nVVMxFzAVBgNVBAgTDk5vcnRoIENhcm9saW5hMRQwEgYDVQQKEwtIeXBlcmxlZGdl\ncjEPMA0GA1UECxMGRmFicmljMRMwEQYDVQQDEwpPcmcxQ0EtdGxzMFkwEwYHKoZI\nzj0CAQYIKoZIzj0DAQcDQgAE5vo/s6vZyAsAVf6j5Wrl/wGVGdvlVgZ0OfvuJQBx\nuHxd82c9YqT71YGgZhn0X+xIKGvx3DT21Q4x9RUR+H6+5qNcMFowDgYDVR0PAQH/\nBAQDAgEGMBIGA1UdEwEB/wQIMAYBAf8CAQEwHQYDVR0OBBYEFAO34fp2LeT/74yu\nwymuI51GrKPCMBUGA1UdEQQOMAyHBLis1oeHBApMwakwCgYIKoZIzj0EAwIDRwAw\nRAIgRY2fyDj6TgQj3UFrP2EVoSl44fQxw6qOIV5bbLJsgsYCIBXZ3PGbrcVODiP5\n564q4uwmyHk+jk/s+AgBboBSmTCK\n-----END CERTIFICATE-----";
		String ordercert = "pems\\order.pem"; 
	    File orderpemfile = new File(ordercert); 			

		Properties orderer_properties = new Properties();
		orderer_properties.setProperty("pemFile", orderpemfile.getAbsolutePath());
		orderer_properties.put("ordererWaitTimeMilliSecs", 200000);
		orderer_properties.setProperty("sslProvider", "openSSL");
		orderer_properties.setProperty("negotiationType", "TLS");
		 //orderer_properties.setProperty("hostnameOverride", "184.172.214.135");

		Orderer orderer = hfClient.newOrderer(orderer_name, orderer_url, orderer_properties);
//////////////////////////////////////order	 end	
		channel = hfClient.newChannel("channel1");

		channel.addPeer(peer);
		 //channel.addEventHub(eventHub);
		channel.addOrderer(orderer);
		channel.initialize();
		System.out.println("channel initialize finish!");
		return channel;		
	}
	//execute invoke 
	public String invoke(String chainCodeName, String fcnName, String[] invokeArgs) throws Exception{
		Channel channel = initChannel();
		TransactionProposalRequest request = hfClient.newTransactionProposalRequest();
		String cc = chainCodeName; // Chaincode name
		ChaincodeID ccid = ChaincodeID.newBuilder().setName(cc).build();

		request.setChaincodeID(ccid);
		request.setFcn(fcnName); // Chaincode invoke funtion name
		request.setArgs(invokeArgs);
		request.setProposalWaitTime(30000);
		Collection<ProposalResponse> responses = channel.sendTransactionProposal(request);
		for (ProposalResponse res : responses) {
			System.out.println(res.getMessage());
			System.out.println(res.getTransactionID());
		}
		CompletableFuture<TransactionEvent> cf = channel.sendTransaction(responses);
		//System.out.println(responses.iterator().next().getProposalResponse().getResponse().getPayload().toStringUtf8());		 
		return null;		
	}
	public static void main(String[] args) throws Exception{
		InvokeBCP invoke = new InvokeBCP();
		QueryBCP query = new QueryBCP();
		String[] invokeArgs= { "802", "bread", "150", "kg", "503","3"};
		String cc = "go_package8";
		String fcnName = "initProfitablesupply";
		invoke.invoke(cc,fcnName,invokeArgs);
		
		cc = "go_package8";
		invokeArgs= new String[]{ "bread"};
		fcnName = "queryProByName";
		query.query(cc,fcnName,invokeArgs);
		

	}
}
