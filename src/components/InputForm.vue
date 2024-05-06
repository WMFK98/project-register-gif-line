<script setup>
defineProps({
  title: String,
  maxLength: String,
  placeholder: String,
  type: { type: String, default: 'text' }
})
</script>
<template>
  <label>
    <p>{{ title }} <span class="text-red-400">*</span></p>
    <input
      :maxlength="maxLength"
      :placeholder="placeholder"
      :type="type"
      class="input input-sm bg-white input-bordered w-full"
    />
  </label>
</template>
