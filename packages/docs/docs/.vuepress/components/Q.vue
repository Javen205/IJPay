<template>
  <div>
    <div class="g-container" @click="toggleButton">
      <div class="g-box">在线客服-付费答疑</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'floatIframe',
  data() {
    return {
      url: ""
    }
  },
  props: {
    url: {
      default: ''
    }
  },
  methods: {
    toggleButton() {
      window.open(this.url, '_blank').location;
    }
  }
}
</script>

<style scoped>
.g-container {
  position: fixed;
  display: flex;
  right: 2%;
  top: 10%;
  z-index: 11;
  width: 180px;
  height: 40px;
  border-radius: 10px;
  background-color: #35bc98;
}

.g-box {
  margin: auto;
  color: #cdf6ee;
  text-shadow: 3px 3px 2px #999;
  text-align: center;
}
</style>