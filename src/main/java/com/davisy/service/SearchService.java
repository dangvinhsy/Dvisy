package com.davisy.service;

import java.util.List;

import com.davisy.mongodb.documents.Search;

public interface SearchService {
	public List<Search> get10Search (String id);
	
	public Search search(String id, String searchText);
	
	
}
