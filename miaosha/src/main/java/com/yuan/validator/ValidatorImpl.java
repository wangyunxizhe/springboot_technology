package com.yuan.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * javabean属性校验类
 */
@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    //实现校验方法并返回校验结果
    public ValidationResult validate(Object bean) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> errSet = validator.validate(bean);
        if (errSet.size() > 0) {//有错误
            result.setHasErrors(true);
            errSet.forEach(err -> {
                String errMsg = err.getMessage();
                String propertyName = err.getPropertyPath().toString();
                result.getErrMsgMap().put(propertyName, errMsg);//{有错属性名=错误信息}
            });
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //实例化validator
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

}
