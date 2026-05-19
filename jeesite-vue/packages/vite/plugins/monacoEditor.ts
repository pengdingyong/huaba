/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { HtmlTagDescriptor, Connect, ResolvedConfig, PluginOption } from 'vite';
import fs from 'fs';
import path from 'path';
import esbuild from 'esbuild';

export function configMonacoEditorPlugin(): PluginOption {
  return monacoEditorPluginJeeSite({
    languageWorkers: ['editorWorkerService', 'json', 'html'],
    customDistPath: (root, buildOutDir) => `${buildOutDir}/monaco`,
    publicPath: 'monaco',
  });
}

// Copyright (c) 2021 vdesjs, thinkgem
//
// This file contains code derived from an MIT-licensed project.
// The MIT License applies **only to the content of this file** and does not
// extend to the rest of the project.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

// src/languageWork.ts

interface IWorkerDefinition {
  label: string;
  entry: string;
}

const languageWorkAttr: IWorkerDefinition[] = [
  {
    label: 'editorWorkerService',
    entry: 'monaco-editor/esm/vs/editor/editor.worker.js',
  },
  {
    label: 'css',
    entry: 'monaco-editor/esm/vs/language/css/css.worker.js',
  },
  {
    label: 'html',
    entry: 'monaco-editor/esm/vs/language/html/html.worker.js',
  },
  {
    label: 'json',
    entry: 'monaco-editor/esm/vs/language/json/json.worker.js',
  },
  {
    label: 'typescript',
    entry: 'monaco-editor/esm/vs/language/typescript/ts.worker.js',
  },
];

const languageWorksByLabel: { [language: string]: IWorkerDefinition } = {};
languageWorkAttr.forEach((languageWork) => (languageWorksByLabel[languageWork.label] = languageWork));

type EditorLanguageWorks = 'css' | 'html' | 'json' | 'typescript' | 'editorWorkerService';

// src/workerMiddleware.ts

function getFilenameByEntry(entry: string) {
  entry = path.basename(entry, '.js');
  return entry + '.bundle.js';
}

const cacheDir = 'node_modules/.monaco/';

function getWorkPath(works: IWorkerDefinition[], options: IMonacoEditorOpts, config: ResolvedConfig) {
  const workerPaths: Record<string, any> = {};
  for (const work of works) {
    if (isCDN(options.publicPath ?? '')) {
      workerPaths[work.label] = options.publicPath + '/' + getFilenameByEntry(work.entry);
    } else {
      workerPaths[work.label] = config.base + options.publicPath + '/' + getFilenameByEntry(work.entry);
    }
  }

  if (workerPaths['typescript']) {
    // javascript shares the same worker
    workerPaths['javascript'] = workerPaths['typescript'];
  }
  if (workerPaths['css']) {
    // scss and less share the same worker
    workerPaths['less'] = workerPaths['css'];
    workerPaths['scss'] = workerPaths['css'];
  }
  if (workerPaths['html']) {
    // handlebars, razor and html share the same worker
    workerPaths['handlebars'] = workerPaths['html'];
    workerPaths['razor'] = workerPaths['html'];
  }

  return workerPaths;
}

function workerMiddleware(middlewares: Connect.Server, config: ResolvedConfig, options: IMonacoEditorOpts): void {
  const works = getWorks(options);
  if (!works) {
    throw new Error('No work definition found.');
  }
  // clear cacheDir

  if (fs.existsSync(cacheDir)) {
    fs.rmSync(cacheDir, { recursive: true, force: true } as fs.RmDirOptions);
  }

  for (const work of works) {
    middlewares.use(config.base + options.publicPath + '/' + getFilenameByEntry(work.entry), function (req, res, next) {
      if (!fs.existsSync(cacheDir + getFilenameByEntry(work.entry))) {
        esbuild.buildSync({
          entryPoints: [resolveMonacoPath(work.entry)],
          bundle: true,
          outfile: cacheDir + getFilenameByEntry(work.entry),
        });
      }
      const contentBuffer = fs.readFileSync(cacheDir + getFilenameByEntry(work.entry));
      res.setHeader('Content-Type', 'text/javascript');
      res.end(contentBuffer);
    });
  }
}

// src/index.ts

/**
 * Return a resolved path for a given Monaco file.
 */
function resolveMonacoPath(filePath: string): string {
  try {
    const fullFilePath = path.resolve(path.join(process.cwd(), 'node_modules', filePath));
    if (fs.existsSync(fullFilePath)) {
      return fullFilePath;
    }
    return filePath;
  } catch (err) {
    throw err;
  }
}

function getWorks(options: IMonacoEditorOpts) {
  let works: IWorkerDefinition[] | undefined = options.languageWorkers?.map((work) => languageWorksByLabel[work]);

  works && options.customWorkers && works.push(...options.customWorkers);

  return works;
}

interface IMonacoEditorOpts {
  /**
   * include only a subset of the languageWorkers supported.
   */
  languageWorkers?: EditorLanguageWorks[];

  customWorkers?: IWorkerDefinition[];

  /**
   * Override the public path from which files generated by this plugin will be served.
   * This wins out over Webpack's dynamic runtime path and can be useful to avoid attempting to load workers cross-
   * origin when using a CDN for other static resources.
   * Use e.g. '/' if you want to load your resources from the current origin.
   */
  publicPath?: string;

  customDistPath?: (root: string, buildOutDir: string, base: string) => string;

  forceBuildCDN?: boolean;

  /**
   * Specify whether the editor API should be exposed through a global `monaco` object or not. This
   * option is applicable to `0.22.0` and newer version of `monaco-editor`. Since `0.22.0`, the ESM
   * version of the monaco editor does no longer define a global `monaco` object unless
   * `global.MonacoEnvironment = { globalAPI: true }` is set ([change
   * log](https://github.com/microsoft/monaco-editor/blob/main/CHANGELOG.md#0220-29012021)).
   */
  globalAPI?: boolean;
}

function monacoEditorPluginJeeSite(options?: IMonacoEditorOpts): PluginOption {
  const languageWorkers = options?.languageWorkers ?? (Object.keys(languageWorksByLabel) as EditorLanguageWorks[]);
  const publicPath = options?.publicPath ?? 'monacoeditorwork';
  const globalAPI = options?.globalAPI ?? false;
  const customWorkers = options?.customWorkers ?? [];
  const forceBuildCDN = options?.forceBuildCDN ?? false;

  options = {
    ...options,
    languageWorkers,
    publicPath,
    globalAPI,
    customWorkers,
    forceBuildCDN,
  };

  let resolvedConfig: ResolvedConfig;

  return {
    name: 'vite-plugin-moncao-editor-jeesite',
    configResolved(getResolvedConfig) {
      resolvedConfig = getResolvedConfig;
    },
    configureServer(server) {
      if (isCDN(publicPath)) {
        return;
      }

      workerMiddleware(server.middlewares, resolvedConfig, options);
    },
    transformIndexHtml(html) {
      const works = getWorks(options);
      if (!works) {
        throw new Error('No work definition found.');
      }
      const workerPaths = getWorkPath(works, options, resolvedConfig);

      const globals = {
        MonacoEnvironment: `(function (paths) {
          return {
            globalAPI: ${globalAPI},
            getWorkerUrl : function (moduleId, label) {
              var result =  paths[label];
              if (/^((http:)|(https:)|(file:)|(\\/\\/))/.test(result)) {
                var currentUrl = String(window.location);
                var currentOrigin = currentUrl.substr(0, currentUrl.length - window.location.hash.length - window.location.search.length - window.location.pathname.length);
                if (result.substring(0, currentOrigin.length) !== currentOrigin) {
                  var js = '/*' + label + '*/importScripts("' + result + '");';
                  var blob = new Blob([js], { type: 'application/javascript' });
                  return URL.createObjectURL(blob);
                }
              }
              return result;
            }
          };
        })(${JSON.stringify(workerPaths, null, 2)})`,
      } as Record<string, any>;

      const descriptor: HtmlTagDescriptor[] = [
        {
          tag: 'script',
          children: Object.keys(globals)
            .map((key) => `self[${JSON.stringify(key)}] = ${globals[key]};`)
            .join('\n'),
          injectTo: 'head-prepend',
        },
      ];
      return descriptor;
    },

    writeBundle() {
      // 是cdn地址并且没有强制构建worker cdn则返回
      if (isCDN(publicPath) && !forceBuildCDN) {
        return;
      }

      const works = getWorks(options);

      const distPath = options.customDistPath
        ? options.customDistPath(resolvedConfig.root, resolvedConfig.build.outDir, resolvedConfig.base)
        : path.join(resolvedConfig.root, resolvedConfig.build.outDir, resolvedConfig.base, options.publicPath ?? '');

      //  console.log("distPath", distPath)

      // write publicPath
      if (!fs.existsSync(distPath)) {
        fs.mkdirSync(distPath, {
          recursive: true,
        });
      }

      if (!works) {
        throw new Error('No work definition found.');
      }
      for (const work of works) {
        if (!fs.existsSync(cacheDir + getFilenameByEntry(work.entry))) {
          const entryPath = resolveMonacoPath(work.entry);
          esbuild.buildSync({
            entryPoints: [entryPath],
            bundle: true,
            outfile: cacheDir + getFilenameByEntry(work.entry),
          });
        }
        const contentBuffer = fs.readFileSync(cacheDir + getFilenameByEntry(work.entry));
        const workDistPath = path.resolve(distPath, getFilenameByEntry(work.entry));
        fs.writeFileSync(workDistPath, contentBuffer);
      }
    },
  };
}

function isCDN(publicPath: string) {
  if (/^((http:)|(https:)|(file:)|(\/\/))/.test(publicPath)) {
    return true;
  }
  return false;
}
