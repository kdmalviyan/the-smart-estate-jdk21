import { RouteInfo } from './sidebar.metadata';

export const ROUTES: RouteInfo[] = [

  {
    path: '/dashboard/main',
    title: 'MENUITEMS.HOME.TEXT',
    moduleName: 'dashboard',
    icon: 'monitor',
    class: '',
    groupTitle: false,
    submenu: []
  },
  {
    path: '/user-management',
    title: 'User Management',
    moduleName: 'user-management',
    icon: 'user',
    class: '',
    groupTitle: false,
    submenu: []
  },
  {
    path: '/follow-up',
    title: 'Follow Up',
    moduleName: 'follow-up',
    icon: 'watch',
    class: '',
    groupTitle: false,
    submenu: []
  },
  {
    path: '/lead-management',
    title: 'Lead Management',
    moduleName: 'user-management',
    icon: 'trending-up',
    class: '',
    groupTitle: false,
    submenu: []
  },
  {
    path: '/raw-lead-management',
    title: 'Raw Lead Management',
    moduleName: 'user-management',
    icon: 'trending-up',
    class: '',
    groupTitle: false,
    submenu: []
  },
  {
    path: '/project-management',
    title: 'Project Management',
    moduleName: 'project-management',
    icon: 'codesandbox',
    class: '',
    groupTitle: false,
    submenu: []
  },
  {
    path: '/team-management',
    title: 'Team Management',
    moduleName: 'team-management',
    icon: 'users',
    class: '',
    groupTitle: false,
    submenu: []
  },
  {
    path: '',
    title: 'Meta Data',
    moduleName: 'metaData',
    icon: 'database',
    class: 'menu-toggle',
    groupTitle: false,
    submenu: [
      {
        path: '/metaData/lead-source',
        title: 'Lead Source',
        moduleName: 'lead-source',
        icon: '',
        class: 'ml-menu',
        groupTitle: false,
        submenu: []
      },
    ]
  },
];
