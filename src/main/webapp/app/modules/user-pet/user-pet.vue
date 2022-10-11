<template>
  <div v-if="user">
    <span>Email: </span>
    <span>{{ user.email }}</span>
  </div>
</template>
<script lang="ts" src="./user-pet.component.ts"></script>
