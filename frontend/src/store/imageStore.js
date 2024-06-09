import { defineStore, acceptHMRUpdate } from 'pinia'
import { previewBinaryFile } from '@/libs/previewBinary'
export const useImageStore = defineStore('ImageStore', {
  state: () => ({ cardImg: null, paymentImg: null }),
  getters: {
    cardImgPreview: (state) => previewBinaryFile(state.cardImg),
    paymentPreview: (state) => previewBinaryFile(state.paymentImg)
  },
  actions: {
    setCardImg(img) {
      this.cardImg = img
    },
    setPaymentImg(img) {
      this.paymentImg = img
    },
    clearAll() {
      this.cardImg = null
      this.paymentImg = null
    }
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useImageStore, import.meta.hot))
}
