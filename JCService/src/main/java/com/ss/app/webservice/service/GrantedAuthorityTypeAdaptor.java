package com.ss.app.webservice.service;

import java.io.IOException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GrantedAuthorityTypeAdaptor extends TypeAdapter<GrantedAuthority> {
		  @Override
		  public void write(JsonWriter out, GrantedAuthority value) throws IOException {
		    new Gson().getAdapter(SimpleGrantedAuthority.class).write(out, (SimpleGrantedAuthority) value);
		  }

		  @Override
		  public GrantedAuthority read(JsonReader in) throws IOException {
		    return new Gson().getAdapter(SimpleGrantedAuthority.class).read(in);
		  }

}
