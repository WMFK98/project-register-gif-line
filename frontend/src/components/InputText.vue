<script setup>
defineProps({
  title: String,
  maxLength: String,
  placeholder: String,
  type: { type: String, default: 'text' },
  modelValue: String,
  errors: { type: Array }
})
</script>
<template>
  <label>
    <p>{{ title }} <span class="text-red-400">*</span></p>
    <input
      :maxlength="maxLength"
      :placeholder="placeholder"
      :type="type"
      @input="$emit('update:modelValue', $event.target.value)"
      class="input input-sm bg-white input-bordered w-full"
      :value="modelValue"
    />
    <div>
      <span v-for="error in errors" :key="error.$uid" class="text-red-500 ml-1 text-[13px]"
        >{{ error.$message }}
      </span>
    </div>
  </label>
</template>
