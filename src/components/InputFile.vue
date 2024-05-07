<script setup>
defineProps({
  title: String
})
</script>
<template>
  <label class="w-full">
    <p>{{ title }}<span class="text-red-400">*</span></p>
    <input
      type="file"
      class="file-input bg-slate-50 file-input-xs file-input-bordered w-full max-w-xs"
    />
  </label>
</template>
