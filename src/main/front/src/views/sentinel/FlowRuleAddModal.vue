<template>
  <a-modal v-model="visible" title="流控规则" @ok="handleOk" width="900px" :dialog-style="{ top: '30px' }">
      <a-form :form="form" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
        <a-form-item label="ID" v-show="false">
          <a-input v-decorator="['id']" />
        </a-form-item>
        <a-form-item label="应用">
          <span>{{app}}</span>
          <a-input v-decorator="['app']" v-show="false"/>
        </a-form-item>
        <a-form-item label="资源名">
          <a-input v-decorator="['resource', { rules: [{ required: true, message: '请输入资源名' }] }]"/>
        </a-form-item>
        <a-form-item label="针对来源">
          <a-input v-decorator="['limitApp', {initialValue:'default', rules: [{ required: true, message: '请输入针对来源' }] }]"/>
        </a-form-item>
        <a-form-item label="阈值类型">
          <a-radio-group v-decorator="['grade', {initialValue:1, rules: [{ required: true, message: '请输入资源名' }] }]">
            <a-radio :value="1">QPS</a-radio>
            <a-radio :value="0">线程数</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="单机阈值">
          <a-input-number style="width:200px" :min="1" v-decorator="['count', {initalValue:'default', rules: [{ required: true, message: '请输入单机阈值' }] }]"/>
        </a-form-item>
        <a-form-item label="流控模式">
          <a-radio-group v-decorator="['strategy', {initialValue:0, rules: [{ required: true, message: '请输入流控模式' }] }]">
            <a-radio :value="0">直接</a-radio>
            <a-radio :value="1">关联</a-radio>
            <a-radio :value="2">链路</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="关联资源" v-if="this.form.getFieldValue('strategy') != 0" >
          <a-input v-decorator="['refResource']"/>
        </a-form-item>
        <a-form-item label="流控效果" v-show="this.form.getFieldValue('grade') == 1">
          <a-radio-group v-decorator="['controlBehavior', {initialValue:0, rules: [{ required: true, message: '请输入流控效果' }] }]">
            <a-radio :value="0">快速失败</a-radio>
            <a-radio :value="2">排队等待</a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="超时时间" v-show="this.form.getFieldValue('controlBehavior') == 2 && this.form.getFieldValue('grade') == 1">
          <a-input v-decorator="['maxQueueingTimeMs']"/>
        </a-form-item>
      </a-form>
    </a-modal>
</template>
<script>
import { saveRule } from '@/api/home'

export default {
  name: 'FlowRuleAddModal',
  components: {

  },
  data () {
    return {
      visible: false,
      form: this.$form.createForm(this, { name: 'dynamic_rule' }),
      app: ''
    }
  },
  methods: {
    showModal (resource) {
      this.visible = true
      this.app = resource.app
      this.form.resetFields()
      this.$nextTick(res => {
        this.form.setFieldsValue(resource)
      })
    },
    handleOk (e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (err) {
          return
        }
        saveRule(values).then(res => {
          if (res.success) {
            this.visible = false
            this.$emit('ok')
          }
        })
      })
    }
  }
}
</script>
