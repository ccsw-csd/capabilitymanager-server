package com.ccsw.capabilitymanager.config.literal;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ccsw.capabilitymanager.config.literal.model.Literal;

import java.util.List;

@Service
@Transactional
public class LiteralServiceImpl implements LiteralService{

    @Autowired
    private LiteralRepository literalRepository;
    
    /**
     * Retrieves a list of all literals, sorted in their natural order.
     *
     * @return A list of {@link Literal} objects representing all literals, sorted by their natural ordering.
     */
    @Override
    public List<Literal> findAll() {    	
        return this.literalRepository.findAll().stream().sorted().toList();
    }

    /**
     * Retrieves a list of literals filtered by type and sorted in natural order.
     *
     * @param type The type identifier used to filter the literals.
     * @return A list of {@link Literal} objects representing literals of the specified type, sorted by their natural ordering.
     */
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
    
    /**
     * Retrieves a list of literals filtered by their subtype.
     *
     * @param subtype The subtype identifier used to filter the literals.
     * @return A list of {@link Literal} objects representing literals with the specified subtype.
     */
    @Override
	public List<Literal> findBySubtype(String subtype) {
		return this.literalRepository.findBySubtype(subtype);
	}
    
    /**
     * Retrieves a literal by its ID.
     *
     * @param id The ID of the literal to retrieve.
     * @return The {@link Literal} object with the specified ID, or {@code null} if no literal with the given ID exists.
     */ 
    @Override
    public Literal findById(Long id) {
        return this.literalRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves a list of literals filtered by both type and subtype. The results are cached to improve performance.
     *
     * @param type The type identifier used to filter the literals.
     * @param subtype The subtype identifier used to further filter the literals.
     * @return A list of {@link Literal} objects representing literals that match the specified type and subtype.
     */
    @Cacheable("findByTypeAndSubtype")
	@Override
	public List<Literal> findByTypeAndSubtype(String type, String subtype) {
		return this.literalRepository.findByTypeAndSubtype(type, subtype);
	}
}
