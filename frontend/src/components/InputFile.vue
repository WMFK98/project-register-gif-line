<script setup>
import { computed } from 'vue'
import { useImageStore } from '@/store/imageStore'
const imageStore = useImageStore()
const { setCardImg, setPaymentImg } = imageStore
const props = defineProps({
  title: String,
  selectText: String,
  defaultText: String,
  imgStored: Object,
  errors: Array
})

const prefix = computed(
  () => props.imgStored.name.split('.')[props.imgStored.name.split('.').length - 1]
)
const nameImage = computed(() =>
  props.imgStored.name.length > 20
    ? props.imgStored.name.slice(0, 20) + '...*' + prefix.value
    : props.imgStored.name
)

const selectImg = async (e) => {
  const file = await e.target.files[0]
  if (props.imgStored === imageStore.cardImg) setCardImg(file)
  else setPaymentImg(file)
}
</script>
<template>
  <label class="w-full">
    <p>{{ title }}<span class="text-red-400">*</span></p>
    <div class="flex items-center cursor-pointer text-[12px] overflow-hidden">
      <p class="bg-primary-100 rounded-l-md text-white border-[1px] p-1">
        {{ selectText || 'เลือกรูปของคุณ' }}
      </p>
      <p class="bg-slate-100 text-primary-100 w-[200px] p-1 rounded-r-md border-[1px]">
        {{ imgStored ? nameImage : defaultText || 'ยังไม่มีไฟล์ สกุล jpg,png,jpeg' }}
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
