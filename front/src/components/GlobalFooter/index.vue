<template>
  <global-footer class="footer custom-render">
    <template v-slot:links>
      <a href="https://www.github.com/vueComponent/pro-layout" target="_blank">使用文档</a>
      <a href="https://www.github.com/sendya/" target="_blank">定制化</a></a>
    </template>
    <template v-slot:copyright>
      <a href="https://github.com/vueComponent" target="_blank">效能中台</a>
    </template>
  </global-footer>
</template>

<script>
import { GlobalFooter } from '@ant-design-vue/pro-layout'

export default {
  name: 'ProGlobalFooter',
  components: {
    GlobalFooter
  }
}
</script>
