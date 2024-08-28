package com.ccsw.capabilitymanager.config.literal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import com.ccsw.capabilitymanager.config.literal.model.Literal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LiteralServiceImplTest {
    @InjectMocks
    private LiteralServiceImpl literalService;

    @Mock
    private LiteralRepository literalRepository;

    private Literal literal1;
    private Literal literal2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        literal1 = new Literal();
        literal1.setId(1L);
        literal1.setSubtype("SubtypeA");
        literal1.setOrd(1);

        literal2 = new Literal();
        literal2.setId(2L);
        literal2.setSubtype("SubtypeB");
        literal2.setOrd(2);
    }

    @Test
    public void testFindAll() {
        List<Literal> literals = Arrays.asList(literal1, literal2);

        when(literalRepository.findAll()).thenReturn(literals);

        List<Literal> result = literalService.findAll();

        assertEquals(2, result.size());
        assertEquals(literal1, result.get(0));
        assertEquals(literal2, result.get(1));
    }

    @Test
    public void testFindByType() {
        String type = "typeA";
        List<Literal> literals = Arrays.asList(literal1);

        when(literalRepository.findByType(type)).thenReturn(literals);

        List<Literal> result = literalService.findByType(type);

        assertEquals(1, result.size());
        assertEquals(literal1, result.get(0));
    }

    @Test
    public void testFindBySubtype() {
        String subtype = "SubtypeA";
        List<Literal> literals = Arrays.asList(literal1);

        when(literalRepository.findBySubtype(subtype)).thenReturn(literals);

        List<Literal> result = literalService.findBySubtype(subtype);

        assertEquals(1, result.size());
        assertEquals(literal1, result.get(0));
    }

    @Test
    public void testFindById() {
        Long id = 1L;

        when(literalRepository.findById(id)).thenReturn(java.util.Optional.ofNullable(literal1));

        Literal result = literalService.findById(id);

        assertEquals(literal1, result);
    }

    @Test
    public void testFindByTypeAndSubtype() {
        String type = "typeA";
        String subtype = "SubtypeA";
        List<Literal> literals = Arrays.asList(literal1);

        when(literalRepository.findByTypeAndSubtype(type, subtype)).thenReturn(literals);

        List<Literal> result = literalService.findByTypeAndSubtype(type, subtype);

        assertEquals(1, result.size());
        assertEquals(literal1, result.get(0));
    }
}
