<script setup>
defineProps({
  text: String,
  isDisabled: Boolean
})
</script>
<template>
  <button
    :disabled="isDisabled"
    class="btn text-primary-100 mx-auto rounded-2xl px-7 text-md p-2 btn-md btn-outline my-5"
  >
    {{ text }}
  </button>
</template>
