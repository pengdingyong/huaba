<template>
  <Card title="项目" v-bind="$attrs">
    <template #extra>
      <a-button type="link" size="small">更多</a-button>
    </template>

    <template v-for="item in groupItems" :key="item.title">
      <Card.Grid class="!w-full !md:w-1/3 cursor-pointer" @click="go(item.url)">
        <span class="flex">
          <Icon :icon="item.icon" :color="item.color" size="30" />
          <span class="ml-4 text-lg">{{ item.title }}</span>
        </span>
        <div class="text-secondary mt-2 h-10 flex">{{ item.desc }}</div>
        <div class="text-secondary flex justify-between">
          <span>{{ item.group }}</span>
          <span>{{ item.date }}</span>
        </div>
      </Card.Grid>
    </template>
  </Card>
</template>
<script lang="ts" setup>
  import { Card } from 'ant-design-vue';
  import { Icon } from '@jeesite/core/components/Icon';
  import { groupItems } from './Data';
  import { useGo } from '@jeesite/core/hooks/web/usePage';

  const go = useGo();
</script>
