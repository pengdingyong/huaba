/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author Vben、ThinkGem
 */
import { computed, unref } from 'vue';

import { useAppStore } from '@jeesite/core/store/modules/app';

import { useRouter } from 'vue-router';

/**
 * @description: Full screen display content
 */
export const useFullContent = () => {
  const appStore = useAppStore();
  const router = useRouter();
  const { currentRoute } = router;

  // Whether to display the content in full screen without displaying the menu
  const getFullContent = computed(() => {
    const route = unref(currentRoute);
    if (route.path.includes('__full__')) {
      return true;
    }
    const query = route.query;
    if (query && Reflect.has(query, '__full__')) {
      return true;
    }
    return appStore.getProjectConfig.fullContent;
  });

  return { getFullContent };
};
