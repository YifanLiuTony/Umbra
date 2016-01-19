package com.website.umbra.server;

import com.website.umbra.client.ClientInfo;

/**
 * The client object. Used to save and load user account data.
 * @author Patrick
 *
 */
public class Client {
	ClientInfo i = null;
	public Client(ClientInfo i ){
		this.i = i;
	}
	public ClientInfo getClientInfo(){
		return this.i;
	}
}
