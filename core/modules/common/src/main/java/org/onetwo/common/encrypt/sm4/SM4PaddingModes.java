package org.onetwo.common.encrypt.sm4;

public enum SM4PaddingModes {

    /**
     *
     */
//    ENCODING("UTF-8"),
//    ALGORITHM_NAME("SM4"),
    CBC_PADDING("SM4/CBC/PKCS5Padding"),
    ECB_PADDING("SM4/ECB/PKCS5Padding");

    ;


    private String name;

    SM4PaddingModes(String msg) {
        this.name = msg;
    }
    
    public boolean isECB() {
    	return name.contains("/ECB/");
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "BaseEnum{" +
                "msg='" + name + '\'' +
                '}';
    }
}
