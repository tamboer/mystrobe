package net.mystrobe.client.ui;

import net.mystrobe.client.config.IBLSessionIDProvider;

import org.apache.wicket.Session;

public class WicketWebApplicationSessionIDProvider implements IBLSessionIDProvider {

	@Override
	public String getBLSessionID() {
		Session session =  Session.get();
		return session.getId();
	}

}
