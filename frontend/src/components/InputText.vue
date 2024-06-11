<script setup>
defineProps({
  title: String,
  maxLength: {
    type: String,
    default: null
  },
  placeholder: String,
  type: { type: String, default: 'text' },
  modelValue: String,
  errors: { type: Array }
})
</script>
<template>
  <label class="relative">
    <p>{{ title }} <span class="text-red-400">*</span></p>
    <input
      :maxlength="maxLength"
      :placeholder="placeholder"
      :type="type"
      @input="$emit('update:modelValue', $event.target.value)"
      class="input pr-[65px] input-sm bg-white input-bordered w-full"
      :value="modelValue"
    />
    <p
      v-show="maxLength"
      class="absolute top-[57%] text-gray-400 right-[10px] text-[12px]"
      :class="modelValue.length >= maxLength && 'text-red-600'"
    >
      {{ modelValue.length }}/{{ maxLength }}
    </p>
    <div>
      <span v-for="error in errors" :key="error.$uid" class="text-red-500 ml-1 text-[13px]"
        >{{ error.$message }}
      </span>
    </div>
  </label>
</template>
