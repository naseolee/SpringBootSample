package com.example.SpringBootSample.form;

import javax.validation.GroupSequence;

//　バリデーションの順番を設定する
@GroupSequence({ ValidGroup1.class, ValidGroup2.class})
public interface GroupOrder {

}
