<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()
onMounted(() => router.push({ path: '/' }))
</script>
<template><div></div></template>
