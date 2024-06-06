<script setup>
import { computed } from 'vue'
import { previewBinaryFile } from './../libs/previewBinary.js'
const props = defineProps({
  title: String,
  selectText: String,
  defaultText: String,
  modelValue: Object,
  errors: Array
})

const prefix = computed(
  () => props.modelValue.name.split('.')[props.modelValue.name.split('.').length - 1]
)
const nameImage = computed(() =>
  props.modelValue.name.length > 20
    ? props.modelValue.name.slice(0, 25) + '...*' + prefix.value
    : props.modelValue.name
)

const emits = defineEmits(['update:modelValue'])
const selectImg = async (e) => {
  const file = await e.target.files[0]
  emits('update:modelValue', { name: file.name, type: file.type, preview: previewBinaryFile(file) })
}
</script>
<template>
  <label class="w-full">
    <p>{{ title }}<span class="text-red-400">*</span></p>
    <div class="flex items-center cursor-pointer text-[12px] overflow-hidden">
      <p class="bg-primary-100 rounded-l-md text-white border-[1px] p-1">
        {{ selectText || 'เลือกรูปของคุณ' }}
      </p>
      <p class="bg-slate-100 text-primary-100 w-[250px] p-1 rounded-r-md border-[1px]">
        {{ modelValue ? nameImage : defaultText || 'ยังไม่มีไฟล์ สกุล jpg,png,jpeg' }}
      </p>
    </div>
    <input @change="selectImg" type="file" class="file-input hidden" />
  </label>
  <div>
    <span v-for="error in errors" :key="error.$uid" class="text-red-500 ml-1 text-[13px]"
      >{{ error.$message }}
    </span>
  </div>
</template>
