package com.binarybrothers.gymflexapi.dtos;

import static org.assertj.core.api.Assertions.assertThat;

import com.binarybrothers.gymflexapi.dtos.subcategory.SubCategoryDTO;
import com.binarybrothers.gymflexapi.controllers.TestUtil;
import org.junit.jupiter.api.Test;

class SubCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubCategoryDTO.class);
        SubCategoryDTO subCategoryDTO1 = new SubCategoryDTO();
        subCategoryDTO1.setId(1L);
        SubCategoryDTO subCategoryDTO2 = new SubCategoryDTO();
        assertThat(subCategoryDTO1).isNotEqualTo(subCategoryDTO2);
        subCategoryDTO2.setId(subCategoryDTO1.getId());
        assertThat(subCategoryDTO1).isEqualTo(subCategoryDTO2);
        subCategoryDTO2.setId(2L);
        assertThat(subCategoryDTO1).isNotEqualTo(subCategoryDTO2);
        subCategoryDTO1.setId(null);
        assertThat(subCategoryDTO1).isNotEqualTo(subCategoryDTO2);
    }
}
