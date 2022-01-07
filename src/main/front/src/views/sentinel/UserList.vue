<template>
  <div class="page-content">
    <div class="table-page-search-wrapper">
    </div>
    <div class="table-operator">
        <a-button type="primary" icon="plus" @click="handleAdd">新建</a-button>
    </div>
    <div>
      <s-table
      ref="table"
      size="default"
      :columns="columns"
      :data="loadData"
      :showPagination="false"
      >
        <span slot="action" slot-scope="text, record">
          <template>
            <a-popconfirm
              title="确定要删除吗?"
              v-if="record.status === 1"
              @confirm="() => handleFlow(record)"
            >
              <a href="javascript:;">删除</a>
            </a-popconfirm>
          </template>
        </span>
      </s-table>
    </div>
    <a-modal v-model="visible" title="用户" @ok="handleOk" width="600px" :dialog-style="{ top: '30px' }">
      <a-form :form="form" :label-col="{ span: 5 }" :wrapper-col="{ span: 17 }">
        <a-form-item label="账号">
          <a-input v-decorator="['name', { rules: [{ required: true, message: '请输入账号' }]}]" />
        </a-form-item>
        <a-form-item label="邮箱">
          <a-input v-decorator="['email', { rules: [{ required: true, message: '请输入邮箱' }]}]" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-decorator="['password', { rules: [{ required: true, message: '请输入密码' }]}]" />
        </a-form-item>
        <a-form-item label="角色">
          <a-radio-group v-decorator="['role', { initialValue:1, rules: [{ required: true, message: '请输入角色' }] }]">
            <a-radio :value="1">普通用户</a-radio>
            <a-radio :value="5">管理员</a-radio>
          </a-radio-group>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script>
import STable from '@/components/Table'
import { getUserList, delUser, saveUser } from '@/api/home'

export default {
  name: 'UserList',
  components: {
    STable
  },
  data () {
    return {
      queryParam: {},
      visible: false,
      form: this.$form.createForm(this, { name: 'dynamic_rule' }),
      columns: [
        {
          title: '用户名',
          dataIndex: 'name'
        }, {
          title: '邮箱',
          dataIndex: 'email'
        }, {
          title: '状态',
          dataIndex: 'status',
          customRender: data => data === 1 ? '正常' : '禁用'
        }, {
          title: '来源',
          dataIndex: 'source'
        }, {
          title: '角色',
          dataIndex: 'role',
          customRender: data => data > 1 ? '管理员' : '普通用户'
        }, {
          title: '操作',
          dataIndex: '',
          fixed: 'right',
          width: 100,
          scopedSlots: { customRender: 'action' }
        }
      ],
      loadData: parameter => {
        return getUserList(Object.assign(parameter, this.queryParam))
          .then(res => {
            return res.data
          })
      }
    }
  },
  methods: {
    handleAdd () {
      this.visible = true
      this.form.resetFields()
    },
     handleFlow (record) {
       delUser(record.id).then(res => {
        this.$refs.table.refresh(true)
       })
     },
     handleOk (e) {
      e.preventDefault()
      this.form.validateFields((err, values) => {
        if (err) {
          return
        }
        saveUser(values).then(res => {
          if (res.success) {
            this.visible = false
            this.$refs.table.refresh(true)
          }
        })
      })
    }
  },
  mounted () {
  }
}
</script>
