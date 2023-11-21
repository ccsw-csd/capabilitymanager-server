package com.ccsw.dashboard.config.literal;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccsw.dashboard.config.literal.model.Literal;

import java.util.List;

@Service
@Transactional
public class LiteralServiceImpl implements LiteralService{

    @Autowired
    private LiteralRepository literalRepository;
    
    @Override
    public List<Literal> findAll() {    	
        return this.literalRepository.findAll().stream().sorted().toList();
    }

    @Override
    public List<Literal> findByType(String type) {    	
    /*
    	Comparator<Literal> comparator = new Comparator<Literal>() {
	    	@Override
	    	public int compare(Literal o1, Literal o2) {
	    		if (o1.getSubtype() != o2.getSubtype())
	    			return o2.getSubtype().compareTo(o1.getSubtype());
	    			//return o2.getSubtype() - o1.getSubtype();
	    		else			
	    			return Integer.valueOf(o1.getOrd()).compareTo(o2.getOrd());
	    	}
    	};
      return this.literalRepository.findByType(type).stream().sorted(comparator).toList();
      return this.literalRepository.findByType(type).stream().sorted((x,y)->x.getSubtype().compareTo(y.getSubtype())).toList();
     */
    	
     return this.literalRepository.findByType(type).stream().sorted().toList();
    }
    
    @Override
	public List<Literal> findBySubtype(String subtype) {
		return this.literalRepository.findBySubtype(subtype);
	}
    
    @Override
    public Literal findById(Long id) {
        return this.literalRepository.findById(id).orElse(null);
    }

	@Override
	public List<Literal> findByTypeAndSubtype(String type, String subtype) {
		return this.literalRepository.findByTypeAndSubtype(type, subtype);
	}
}
