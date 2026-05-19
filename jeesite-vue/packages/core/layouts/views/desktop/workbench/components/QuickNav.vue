<template>
  <Card title="快捷导航" v-bind="$attrs">
    <template v-for="item in navItems" :key="item.title">
      <Card.Grid class="cursor-pointer" @click="go(item.url)">
        <span class="flex flex-col items-center">
          <Icon :icon="item.icon" :color="item.color" size="20" />
          <span class="text-md mt-2">{{ item.title }}</span>
        </span>
      </Card.Grid>
    </template>
  </Card>
</template>
<script lang="ts" setup>
  import { Card } from 'ant-design-vue';
  import { navItems } from './Data';
  import { Icon } from '@jeesite/core/components/Icon';
  import { useGo } from '@jeesite/core/hooks/web/usePage';

  const go = useGo();
</script>
