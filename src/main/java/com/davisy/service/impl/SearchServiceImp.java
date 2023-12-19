package com.davisy.service.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.davisy.mongodb.MongoDBUtils;
import com.davisy.mongodb.documents.Search;
import com.davisy.service.SearchService;

@Service
public class SearchServiceImp implements SearchService{

	@Autowired
	MongoDBUtils dbUtils;

	@Value("${davis.mongodb.collectionSearch}")
	private String collectionSearch;
	
	@Override
	public List<Search> get10Search(String id) {
		try {
//			List<Search> searchs = dbUtils.findAll(Search.class, collectionSearch);
			List<Search> searchs = dbUtils.findAllByColumn(Search.class , collectionSearch, "user_id", id );
			if(searchs.size() == 0)
				return null;
			Collections.reverse(searchs);
			if(searchs.size()<10)
				return searchs;
			searchs = searchs.subList(searchs.size()-10, searchs.size());
			return searchs;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Search search(String id, String searchText) {
		try {
			boolean toUpdate = false;
			Search exp = (Search) dbUtils.findByColumn(Search.class, collectionSearch, "content", searchText.trim());
			if(exp != null) {
				toUpdate = true;
				System.out.println("DACO");
			}
			Search search = new Search();
			Calendar calendar = GregorianCalendar.getInstance();
			Date time = calendar.getTime();
			
			search.setContent(searchText.trim());
			search.setDate(time);
			search.setUser_id(id);
			if(toUpdate)
				dbUtils.deletesByColumn(Search.class, collectionSearch, "content", searchText.trim()); 
			dbUtils.insert(search, Search.class, collectionSearch);
			return search;
		} catch (Exception e) {
			return null;
		}
	}

}
