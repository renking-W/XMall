<script setup>
defineProps({
  msg: {
    type: String,
    required: true
  }
})
</script>

<template>
  <div class="hello">
    <h2>{{ msg }}</h2>
    <p>
      这是一个简单的Vue 3组件，使用&lt;script setup&gt;语法。
    </p>
  </div>
</template>

<style scoped>
.hello {
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin: 20px 0;
}
</style>