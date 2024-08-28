package com.ccsw.capabilitymanager.config.literal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import com.ccsw.capabilitymanager.config.literal.model.Literal;
import com.ccsw.capabilitymanager.config.literal.model.LiteralDto;
import org.dozer.DozerBeanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LiteralControllerTest {
    @InjectMocks
    private LiteralController literalController;

    @Mock
    private LiteralService literalService;

    @Mock
    private DozerBeanMapper mapper;

    private Literal literal1;
    private Literal literal2;
    private LiteralDto literalDto1;
    private LiteralDto literalDto2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        literal1 = new Literal();
        literal1.setId(1L);

        literal2 = new Literal();
        literal2.setId(2L);

        literalDto1 = new LiteralDto();
        literalDto1.setId(1);

        literalDto2 = new LiteralDto();
        literalDto2.setId(2);
    }

    @Test
    public void testFindAll() {
        List<Literal> literals = Arrays.asList(literal1, literal2);

        when(literalService.findAll()).thenReturn(literals);
        when(mapper.map(literal1, LiteralDto.class)).thenReturn(literalDto1);
        when(mapper.map(literal2, LiteralDto.class)).thenReturn(literalDto2);

        List<LiteralDto> result = literalController.findAll();

        assertEquals(2, result.size());
        assertEquals(literalDto1, result.get(0));
        assertEquals(literalDto2, result.get(1));
    }

    @Test
    public void testFindAllByType() {
        String typeId = "typeA";
        List<Literal> literals = Arrays.asList(literal1);

        when(literalService.findByType(typeId)).thenReturn(literals);
        when(mapper.map(literal1, LiteralDto.class)).thenReturn(literalDto1);

        List<LiteralDto> result = literalController.findAllByType(typeId);

        assertEquals(1, result.size());
        assertEquals(literalDto1, result.get(0));
    }

    @Test
    public void testFindAllByTypeAndSubtype() {
        String typeId = "typeA";
        String subtypeId = "subtypeA";
        List<Literal> literals = Arrays.asList(literal2);

        when(literalService.findByTypeAndSubtype(typeId, subtypeId)).thenReturn(literals);
        when(mapper.map(literal2, LiteralDto.class)).thenReturn(literalDto2);

        List<LiteralDto> result = literalController.findAllByType(typeId, subtypeId);

        assertEquals(1, result.size());
        assertEquals(literalDto2, result.get(0));
    }

    @Test
    public void testFindById() {
        Long id = 1L;

        when(literalService.findById(id)).thenReturn(literal1);

        Literal result = literalController.findById(String.valueOf(id));

        assertEquals(literal1, result);
    }
}
