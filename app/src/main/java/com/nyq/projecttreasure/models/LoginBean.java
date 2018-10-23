package com.nyq.projecttreasure.models;

import java.util.List;

/**
 * project name wisdom_agriculture
 * package name com.gstb.agriculture.entity
 * Create by lxg
 * on 2017/7/27
 * at 20:43
 */
public class LoginBean {


    /**
     * msg : null
     * code : 200
     * data : {"address":"1","block":[{"block_create_time":"2017-07-26 16:33:45","block_id":"369d087a71dd11e7ae6efcaa145e7e0f","block_name":"六号田","create_time":"2017-07-26  16:33:45","description":"西红柿管家xxxxxx","id":"369d641171dd11e7ae6efcaa145e7e0f","member_id":"1","member_mobile":"1","member_name":"admin","pass":"-1,1","status":"0"}],"create_by":null,"create_time":"1","enabled":"0","id":"1","login_account":"admin","login_pwd":null,"mobile":"1","name":"admin","org_id":"1","salt":null,"status":"0"}
     */

    private Object msg;
    private int code;
    private DataBean data;

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * address : 1
         * block : [{"block_create_time":"2017-07-26 16:33:45","block_id":"369d087a71dd11e7ae6efcaa145e7e0f","block_name":"六号田","create_time":"2017-07-26  16:33:45","description":"西红柿管家xxxxxx","id":"369d641171dd11e7ae6efcaa145e7e0f","member_id":"1","member_mobile":"1","member_name":"admin","pass":"-1,1","status":"0"}]
         * create_by : null
         * create_time : 1
         * enabled : 0
         * id : 1
         * login_account : admin
         * login_pwd : null
         * mobile : 1
         * name : admin
         * org_id : 1
         * salt : null
         * status : 0
         */

        private String address;
        private Object create_by;
        private String create_time;
        private String enabled;
        private String head_portrait;
        private String id;
        private String login_account;
        private Object login_pwd;
        private String mobile;
        private String name;
        private String org_id;
        private String qr_code;
        private Object salt;
        private String status;
        private String uuid;
        private List<BlockBean> block;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getHead_portrait() {
            return head_portrait;
        }

        public void setHead_portrait(String head_portrait) {
            this.head_portrait = head_portrait;
        }

        public String getQr_code() {
            return qr_code;
        }

        public void setQr_code(String qr_code) {
            this.qr_code = qr_code;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getCreate_by() {
            return create_by;
        }

        public void setCreate_by(Object create_by) {
            this.create_by = create_by;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getEnabled() {
            return enabled;
        }

        public void setEnabled(String enabled) {
            this.enabled = enabled;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLogin_account() {
            return login_account;
        }

        public void setLogin_account(String login_account) {
            this.login_account = login_account;
        }

        public Object getLogin_pwd() {
            return login_pwd;
        }

        public void setLogin_pwd(Object login_pwd) {
            this.login_pwd = login_pwd;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrg_id() {
            return org_id;
        }

        public void setOrg_id(String org_id) {
            this.org_id = org_id;
        }

        public Object getSalt() {
            return salt;
        }

        public void setSalt(Object salt) {
            this.salt = salt;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<BlockBean> getBlock() {
            return block;
        }

        public void setBlock(List<BlockBean> block) {
            this.block = block;
        }

        public static class BlockBean {
            /**
             * block_create_time : 2017-07-26 16:33:45
             * block_id : 369d087a71dd11e7ae6efcaa145e7e0f
             * block_name : 六号田
             * create_time : 2017-07-26  16:33:45
             * description : 西红柿管家xxxxxx
             * id : 369d641171dd11e7ae6efcaa145e7e0f
             * member_id : 1
             * member_mobile : 1
             * member_name : admin
             * pass : -1,1
             * status : 0
             */

            private String block_create_time;
            private String block_id;
            private String block_name;
            private String create_time;
            private String description;
            private String id;
            private String member_id;
            private String member_mobile;
            private String member_name;
            private String pass;
            private String status;

            public String getBlock_create_time() {
                return block_create_time;
            }

            public void setBlock_create_time(String block_create_time) {
                this.block_create_time = block_create_time;
            }

            public String getBlock_id() {
                return block_id;
            }

            public void setBlock_id(String block_id) {
                this.block_id = block_id;
            }

            public String getBlock_name() {
                return block_name;
            }

            public void setBlock_name(String block_name) {
                this.block_name = block_name;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public String getMember_mobile() {
                return member_mobile;
            }

            public void setMember_mobile(String member_mobile) {
                this.member_mobile = member_mobile;
            }

            public String getMember_name() {
                return member_name;
            }

            public void setMember_name(String member_name) {
                this.member_name = member_name;
            }

            public String getPass() {
                return pass;
            }

            public void setPass(String pass) {
                this.pass = pass;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
