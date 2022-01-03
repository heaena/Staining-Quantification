<template>
  <div class="main">
    <a-form
      id="formLogin"
      class="user-layout-login"
      ref="formLogin"
      :form="form"
      @submit="handleSubmit"
    >
      <a-form-item>
        <a-input
          size="large"
          type="text"
          placeholder="请输入用户名"
          v-decorator="[
            'username',
            {rules: [{ required: true, message: '请输入帐户名或邮箱地址' }], validateTrigger: 'change'}
          ]"
        >
          <a-icon slot="prefix" type="user" :style="{ color: 'rgba(0,0,0,.25)' }"/>
        </a-input>
      </a-form-item>
      <a-form-item>
        <a-input-password
          size="large"
          placeholder="密码: admin or ant.design"
          v-decorator="[
            'password',
            {rules: [{ required: true, message: '请输入密码' }], validateTrigger: 'blur'}
          ]"
        >
          <a-icon slot="prefix" type="lock" :style="{ color: 'rgba(0,0,0,.25)' }"/>
        </a-input-password>
      </a-form-item>
      <a-form-item style="margin-top:24px">
        <a-button
          size="large"
          type="primary"
          htmlType="submit"
          class="login-button"
          :loading="state.loginBtn"
          :disabled="state.loginBtn"
        >确定</a-button>
      </a-form-item>

      <div class="user-login-other">
        <div style="font-weight:600;">推荐登录方式</div>
        <div style="padding: 20px 0px">
          <a href="/oauth/render/gitlab" title="gitlab" class="item">
            <a-icon class="item-icon" type="gitlab"></a-icon>
            <div class="title">gitlab</div>
          </a>
        </div>
      </div>
    </a-form>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import md5 from 'md5'

export default {
  components: {
  },
  data () {
    return {
      customActiveKey: 'tab1',
      loginBtn: false,
      // login type: 0 email, 1 username, 2 telephone
      loginType: 0,
      isLoginError: false,
      requiredTwoStepCaptcha: false,
      stepCaptchaVisible: false,
      form: this.$form.createForm(this),
      state: {
        time: 60,
        loginBtn: false,
        // login type: 0 email, 1 username, 2 telephone
        loginType: 0,
        smsSendBtn: false
      }
    }
  },
  methods: {
    ...mapActions(['Login', 'Logout']),
    handleSubmit (e) {
      e.preventDefault()
      const {
        form: { validateFields },
        state,
        customActiveKey,
        Login
      } = this
      state.loginBtn = true
      const validateFieldsKey = customActiveKey === 'tab1' ? ['username', 'password'] : ['mobile', 'captcha']
      validateFields(validateFieldsKey, { force: true }, (err, values) => {
        if (!err) {
          console.log('login form', values)
          const loginParams = { ...values }
          delete loginParams.username
          loginParams['username'] = values.username
          loginParams.password = md5(values.password)
          Login(loginParams)
            .then((res) => this.loginSuccess(res))
            .catch(err => this.requestFailed(err))
            .finally(() => {
              state.loginBtn = false
            })
        } else {
          setTimeout(() => {
            state.loginBtn = false
          }, 600)
        }
      })
    },
    loginSuccess (res) {
      this.$router.push({ path: '/admin' })
      this.isLoginError = false
    },
    requestFailed (err) {
      this.isLoginError = true
      console.log(err)
      this.$notification['error']({
        message: '错误',
        description: err.msg || '请求出现错误，请稍后再试',
        duration: 4
      })
    }
  }
}
</script>

<style lang="less" scoped>
.user-layout-login {
  label {
    font-size: 14px;
  }

  .getCaptcha {
    display: block;
    width: 100%;
    height: 40px;
  }

  .forge-password {
    font-size: 14px;
  }

  button.login-button {
    padding: 0 15px;
    font-size: 16px;
    height: 40px;
    width: 100%;
  }

  .user-login-other {
    text-align: left;
    margin-top: 24px;
    line-height: 22px;
    .item{
      display: inline-block;
      padding: 5px 20px;
      border-radius: 5px;
      background: #fff;
      text-align: center;
      border: 1px solid #1890ff;
      .title{
        color:#1890ff;
      }
      &:hover{
        background: #1890ff;
        .title,.item-icon{
          color:#fff;
        }
      }
    }
    .item-icon {
      font-size: 32px;
      color: #1890ff;
      vertical-align: middle;
      cursor: pointer;
      transition: color 0.3s;
    }

    .register {
      float: right;
    }
  }
}
</style>
