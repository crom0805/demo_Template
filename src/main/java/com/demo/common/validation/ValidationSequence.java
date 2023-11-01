package com.demo.common.validation;

import com.demo.common.validation.ValidationGroups.LengthCheckGroup;
import com.demo.common.validation.ValidationGroups.NotEmptyGroup;
import com.demo.common.validation.ValidationGroups.NotNullGroup;
import com.demo.common.validation.ValidationGroups.PatternCheckGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({Default.class, NotEmptyGroup.class, NotNullGroup.class, LengthCheckGroup.class, PatternCheckGroup.class})
public interface ValidationSequence {

}
