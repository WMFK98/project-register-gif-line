<script setup>
import { previewBinaryFile } from './../libs/previewBinary.js'
defineProps({
  title: String,
  modelValue: Object,
  errors: Array
})
const emits = defineEmits(['update:modelValue'])
const selectImg = async (e) => {
  const file = await e.target.files[0]
  emits('update:modelValue', { name: file.name, type: file.type, preview: previewBinaryFile(file) })
}
</script>
<template>
  <label class="w-full">
    <p>{{ title }}<span class="text-red-400">*</span></p>
    <input
      @change="selectImg"
      type="file"
      class="file-input bg-slate-50 file-input-xs file-input-bordered w-full max-w-xs"
    />
  </label>
  <div>
    <span v-for="error in errors" :key="error.$uid" class="text-red-500 ml-1 text-[13px]"
      >{{ error.$message }}
    </span>
  </div>
</template>
