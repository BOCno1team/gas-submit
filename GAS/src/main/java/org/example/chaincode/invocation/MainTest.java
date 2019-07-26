package main.java.org.example.chaincode.invocation;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Properties;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;

import main.java.org.example.user.UserContext;
import main.java.org.example.util.Util;

public class MainTest {
	public static void main(String[] args) throws Exception{
		
		
		String caUrl = "https://184.172.214.135:30935"; // ensure that port is of CA
		String caName = "ca";
		String pemStr = "LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tDQpNSUlDZXpDQ0FlU2dBd0lCQWdJSmM0UmRXNnZpVm5SaU1BMEdDU3FHU0liM0RRRUJCUVVBTUhVeEdEQVdCZ05WDQpCQU1URHpFNE5DNHhOekl1TWpFMExqRXpOVEVMTUFrR0ExVUVCaE1DVlZNeEZ6QVZCZ05WQkFnVERrNXZjblJvDQpJRU5oY205c2FXNWhNUkF3RGdZRFZRUUhFd2RTWVd4bGFXZG9NUXd3Q2dZRFZRUUtFd05KUWsweEV6QVJCZ05WDQpCQXNUQ2tKc2IyTnJZMmhoYVc0d0hoY05NVGt3TmpJMU1EVTBNakV3V2hjTk1qQXdOakkwTURVME1qRXdXakIxDQpNUmd3RmdZRFZRUURFdzh4T0RRdU1UY3lMakl4TkM0eE16VXhDekFKQmdOVkJBWVRBbFZUTVJjd0ZRWURWUVFJDQpFdzVPYjNKMGFDQkRZWEp2YkdsdVlURVFNQTRHQTFVRUJ4TUhVbUZzWldsbmFERU1NQW9HQTFVRUNoTURTVUpODQpNUk13RVFZRFZRUUxFd3BDYkc5amEyTm9ZV2x1TUlHZk1BMEdDU3FHU0liM0RRRUJBUVVBQTRHTkFEQ0JpUUtCDQpnUURBOEtOZ2NGNkZtS20wdkRhdTRrNi9UZ0hNbUFxMkczUE11SlpYbTlCUE1nNEVCRk9tamdXYWxjYUpZZWZxDQpNQWJRVURqT2pObE5FME1jVm11TE5ubjdIQXlYOHJzb0xydHlvVTdwZ3pGTWtmMVJpZmhaUjBDMFJFU21KUjhkDQozbmVjOEJ6dER4YnZnakZ1eGkzc1pML3VGZ1ZvUG1jc1hvZjRjMnhHUmhTdjRRSURBUUFCb3hNd0VUQVBCZ05WDQpIUkVFQ0RBR2h3UzRyTmFITUEwR0NTcUdTSWIzRFFFQkJRVUFBNEdCQUg3VjZzbVFubzRBV1JnbnRCVEVpUXJ2DQpqUVVSMlVhMU43YkJTeC9qVk9rWnhzL045elhKd2NoM0JvYWxGMHhDd0laK04yNDgzY0RGMHl5NlN0ekFiMlJHDQpHdWZEcHlRZFhoa3NVUkhleDk2c2g1MHZxVFNuTGxXa0xGNk0vbWJtaEI5WERnSEgzSHM1NU4vMXpqaEdSUU5pDQplTmVoKzBvTDAyYmFMWVo5S1RDbg0KLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQ0K";
		String cert = "F:\\CallForCode\\fabric-sdk-java-object\\cacert.pem"; 
        File cf = new File(cert); 
		Properties properties = new Properties();
		properties.setProperty("pemFile", cf.getAbsolutePath());
		properties.setProperty("allowAllHostNames", "true");
		HFCAClient hfcaClient = HFCAClient.createNewInstance(caName, caUrl, properties);
		CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
		hfcaClient.setCryptoSuite(cryptoSuite);
		UserContext adminUserContext = new UserContext();
		adminUserContext.setName("ca"); // admin username
		adminUserContext.setAffiliation("org1"); // affiliation
		adminUserContext.setMspId("org1msp"); // org1 mspid
		
		Enrollment adminEnrollment = hfcaClient.enroll("admin", "adminpw"); //pass admin username and password
		adminUserContext.setEnrollment(adminEnrollment);
		Util.writeUserContext(adminUserContext); // save admin context to local file system
		//ok
		
		
		
		
		
		UserContext userContext = new UserContext();
		userContext.setName("peer5");
		userContext.setAffiliation("org1");
		userContext.setMspId("org1msp");

		RegistrationRequest rr = new RegistrationRequest("peer5", "org1");
		String enrollmentSecret = hfcaClient.register(rr, adminUserContext);
		Enrollment enrollment = hfcaClient.enroll(userContext.getName(), enrollmentSecret);
		userContext.setEnrollment(enrollment);
		Util.writeUserContext(userContext);
		
		System.out.println("register user finish");
		
	}
}
