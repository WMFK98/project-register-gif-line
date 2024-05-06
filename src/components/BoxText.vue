<script setup>
defineProps({
  text: String
})
</script>
<template>
  <div class="bg-primary-200 w-[80%] h-[30px] rounded-xl flex justify-center items-center">
    {{ text }}
  </div>
</template>
