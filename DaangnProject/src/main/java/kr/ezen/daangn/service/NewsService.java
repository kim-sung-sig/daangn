package kr.ezen.daangn.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Component;

import com.googlecode.jslint4java.UnicodeBomInputStream;

import kr.ezen.daangn.vo.RssVO;
import kr.ezen.daangn.vo.RssVO.Item;

@Component(value = "newsService")
public class NewsService {
	
	public List<Item> getNewsByUrl(String urlAddress){
		List<Item> items = null;
		URL url;
		JAXBContext context;
		RssVO vo = null;
		try {
			context = JAXBContext.newInstance(RssVO.class);
			Unmarshaller um = context.createUnmarshaller();
			url = new URL(urlAddress);
			InputStream is = url.openStream();
			UnicodeBomInputStream is2 = new UnicodeBomInputStream(is);
			is2.skipBOM();
			InputStreamReader isr = new InputStreamReader(is2);
			vo = (RssVO) um.unmarshal(isr);
			items = new ArrayList<>();
			for(Item item : vo.getChannel().getItem()) {
				items.add(item);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return items;
	}
}
