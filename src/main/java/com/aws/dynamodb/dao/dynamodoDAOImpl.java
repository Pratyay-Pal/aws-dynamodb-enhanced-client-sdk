package com.aws.dynamodb.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.aws.dynamodb.constants.ConfigConstants;
import com.aws.dynamodb.entity.users;

import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.DeleteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Component
public class dynamodoDAOImpl implements dynamodbDAO{

	private Logger logger = LogManager.getLogger(dynamodoDAOImpl.class);
	
	@Override
	public List<users> getAllUsers() {
		//ALL ITEMS SCAN
		DynamoDbClient ddb = createDynamodbClient();
		DynamoDbEnhancedClient enhancedClient = createDynamodbEnhancedClient(ddb);
		DynamoDbTable<users> dbTable = enhancedClient.table(ConfigConstants.user_table_name, TableSchema.fromBean(users.class));
		List<users> userList = new ArrayList<>();
		try {
			Iterator<users> iterator = dbTable.scan().items().iterator();	
			ddb.close();
			while(iterator.hasNext()) {
				userList.add(iterator.next());
			}
			logger.info("All Users Retrieved");
		} catch(Exception e) {
			ddb.close();
			e.printStackTrace();
			logger.error("ERROR");
		}
		return userList;
	}

	@Override
	public users getUserDetail(String user_id) {
		//SINGLE ITEM RETRIEVAL
		DynamoDbClient ddb = createDynamodbClient();
		DynamoDbEnhancedClient enhancedClient = createDynamodbEnhancedClient(ddb);
		DynamoDbTable<users> dbTable = enhancedClient.table(ConfigConstants.user_table_name, TableSchema.fromBean(users.class));
		Key key = Key.builder().partitionValue(user_id).build();
		users user = null;
		try {
			user = dbTable.getItem((GetItemEnhancedRequest.Builder builder) -> builder.key(key));
			if(user == null) 
				logger.info("User details not found");			
			else 
				logger.info("User details retrieved successfully");			
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("ERROR");
		}
		ddb.close();
		return user;
	}

	@Override
	public boolean createUser(String name, String ign, String email) {
		//NEW USER
		DynamoDbClient ddb = createDynamodbClient();
		DynamoDbEnhancedClient enhancedClient = createDynamodbEnhancedClient(ddb);
		DynamoDbTable<users> dbTable = enhancedClient.table(ConfigConstants.user_table_name, TableSchema.fromBean(users.class));
		Iterator<users> iterator =  dbTable.scan().items().iterator();
		String maxID = "user_000";
		while(iterator.hasNext()) {
			String currID = iterator.next().getUser_id();
			if(currID.compareTo(maxID) > 0) {
				maxID = currID;
			}
		}
		int nextID = Integer.parseInt(maxID.split("_")[1])+1;
		users user = new users();
		user.setEmail(email);
		user.setIgn(ign);
		user.setName(name);
		user.setUser_id("user_"+String.format("%03d", nextID));
		logger.info("Creating new User in DynamoDB with details "+user.toString());
		try {
			dbTable.putItem(user);
			logger.info("User Created successfully");
		} catch(Exception e){
			e.printStackTrace();
			logger.error("ERROR");
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteUser(String user_id) {
		//DELETE USER
		DynamoDbClient ddb = createDynamodbClient();
		DynamoDbEnhancedClient enhancedClient = createDynamodbEnhancedClient(ddb);
		DynamoDbTable<users> dbTable = enhancedClient.table(ConfigConstants.user_table_name, TableSchema.fromBean(users.class));
		Key key = Key.builder().partitionValue(user_id).build();
		try {
			dbTable.deleteItem((DeleteItemEnhancedRequest.Builder builder)->builder.key(key));
			logger.info(user_id+" deleted successfully");
		} catch(Exception e){
			e.printStackTrace();
			logger.error("ERROR");
			return false;			
		}
		return true;
	}

	@Override
	public users getUserByEmail(String email) {
		//GET USER BY EMAIL(GLOBAL SECONDARY INDEX)
		//MUST CREATE SECONDARY PARTITION KEY IN MAPPING CLASS
		DynamoDbClient ddb = createDynamodbClient();
		DynamoDbEnhancedClient enhancedClient = createDynamodbEnhancedClient(ddb);
		DynamoDbTable<users> dbTable = enhancedClient.table(ConfigConstants.user_table_name, TableSchema.fromBean(users.class));
		Key key = Key.builder().partitionValue(email).build();
		QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
		SdkIterable<Page<users>> pagedResults = dbTable.index("email-index").query(queryConditional);
		List<users> userList = new ArrayList<>();
		pagedResults.stream().forEach(user -> user.items().stream()
                        .forEach(mt -> {
                            logger.info(mt.toString());
                            userList.add(mt);
                        }));
		if(userList.size() == 0)
			return null;
		users user = userList.get(0);
		return user;
	}
	
	public DynamoDbClient createDynamodbClient() {
		DynamoDbClient ddb = DynamoDbClient.builder().region(ConfigConstants.defaultRegion).build();
		return ddb;
	}
	
	public DynamoDbEnhancedClient createDynamodbEnhancedClient(DynamoDbClient ddb) {
		DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(ddb).build();
		return enhancedClient;
	}	
}
