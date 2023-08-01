package com.example.demo.common.validation;

import com.example.demo.common.validation.ValidationGroups.LengthCheckGroup;
import com.example.demo.common.validation.ValidationGroups.NotEmptyGroup;
import com.example.demo.common.validation.ValidationGroups.PatternCheckGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({Default.class, NotEmptyGroup.class, LengthCheckGroup.class, PatternCheckGroup.class})
public interface ValidationSequence {

}
