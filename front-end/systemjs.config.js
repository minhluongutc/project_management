(function (global) {
    SystemJS.config({
        transpiler: './plugin-typescript.js',
        typescriptOptions: {
            "target": "ES2022",
            "module": "system",
            "emitDecoratorMetadata": true,
            "experimentalDecorators": true,
        },
        baseURL: 'node_modules/',
        meta: {
            'typescript': {
                "exports": "ts"
            },
            '*.css': { loader: 'systemjs-plugin-css' }
        },
        paths: {
            // paths serve as alias
            'npm:': ''
        },
        packageConfigPaths: [
            '/node_modules/*/package.json',
            "/node_modules/@angular/*/package.json",
            "/node_modules/@mescius/*/package.json"
        ],
        map: {
            'core-js': 'https://cdn.jsdelivr.net/npm/core-js@2.6.12/client/shim.min.js',
            'typescript': 'https://cdnjs.cloudflare.com/ajax/libs/typescript/5.2.2/typescript.min.js',
            "rxjs": "https://cdnjs.cloudflare.com/ajax/libs/rxjs/7.8.1/rxjs.umd.min.js",
            'systemjs-plugin-css': 'https://cdn.jsdelivr.net/npm/systemjs-plugin-css@0.1.37/css.js',

            '@mescius/wijmo': 'npm:@mescius/wijmo/index.js',
            '@mescius/wijmo.input': 'npm:@mescius/wijmo.input/index.js',
            '@mescius/wijmo.styles': 'npm:@mescius/wijmo.styles',
            '@mescius/wijmo.cultures': 'npm:@mescius/wijmo.cultures',
            '@mescius/wijmo.chart': 'npm:@mescius/wijmo.chart/index.js',
            '@mescius/wijmo.chart.analytics': 'npm:@mescius/wijmo.chart.analytics/index.js',
            '@mescius/wijmo.chart.animation': 'npm:@mescius/wijmo.chart.animation/index.js',
            '@mescius/wijmo.chart.annotation': 'npm:@mescius/wijmo.chart.annotation/index.js',
            '@mescius/wijmo.chart.finance': 'npm:@mescius/wijmo.chart.finance/index.js',
            '@mescius/wijmo.chart.finance.analytics': 'npm:@mescius/wijmo.chart.finance.analytics/index.js',
            '@mescius/wijmo.chart.hierarchical': 'npm:@mescius/wijmo.chart.hierarchical/index.js',
            '@mescius/wijmo.chart.interaction': 'npm:@mescius/wijmo.chart.interaction/index.js',
            '@mescius/wijmo.chart.radar': 'npm:@mescius/wijmo.chart.radar/index.js',
            '@mescius/wijmo.chart.render': 'npm:@mescius/wijmo.chart.render/index.js',
            '@mescius/wijmo.chart.webgl': 'npm:@mescius/wijmo.chart.webgl/index.js',
            '@mescius/wijmo.chart.map': 'npm:@mescius/wijmo.chart.map/index.js',
            '@mescius/wijmo.gauge': 'npm:@mescius/wijmo.gauge/index.js',
            '@mescius/wijmo.grid': 'npm:@mescius/wijmo.grid/index.js',
            '@mescius/wijmo.grid.detail': 'npm:@mescius/wijmo.grid.detail/index.js',
            '@mescius/wijmo.grid.filter': 'npm:@mescius/wijmo.grid.filter/index.js',
            '@mescius/wijmo.grid.search': 'npm:@mescius/wijmo.grid.search/index.js',
            '@mescius/wijmo.grid.grouppanel': 'npm:@mescius/wijmo.grid.grouppanel/index.js',
            '@mescius/wijmo.grid.multirow': 'npm:@mescius/wijmo.grid.multirow/index.js',
            '@mescius/wijmo.grid.transposed': 'npm:@mescius/wijmo.grid.transposed/index.js',
            '@mescius/wijmo.grid.transposedmultirow': 'npm:@mescius/wijmo.grid.transposedmultirow/index.js',
            '@mescius/wijmo.grid.pdf': 'npm:@mescius/wijmo.grid.pdf/index.js',
            '@mescius/wijmo.grid.sheet': 'npm:@mescius/wijmo.grid.sheet/index.js',
            '@mescius/wijmo.grid.xlsx': 'npm:@mescius/wijmo.grid.xlsx/index.js',
            '@mescius/wijmo.grid.selector': 'npm:@mescius/wijmo.grid.selector/index.js',
            '@mescius/wijmo.grid.cellmaker': 'npm:@mescius/wijmo.grid.cellmaker/index.js',
            '@mescius/wijmo.nav': 'npm:@mescius/wijmo.nav/index.js',
            '@mescius/wijmo.odata': 'npm:@mescius/wijmo.odata/index.js',
            '@mescius/wijmo.olap': 'npm:@mescius/wijmo.olap/index.js',
            '@mescius/wijmo.rest': 'npm:@mescius/wijmo.rest/index.js',
            '@mescius/wijmo.pdf': 'npm:@mescius/wijmo.pdf/index.js',
            '@mescius/wijmo.pdf.security': 'npm:@mescius/wijmo.pdf.security/index.js',
            '@mescius/wijmo.viewer': 'npm:@mescius/wijmo.viewer/index.js',
            '@mescius/wijmo.xlsx': 'npm:@mescius/wijmo.xlsx/index.js',
            '@mescius/wijmo.undo': 'npm:@mescius/wijmo.undo/index.js',
            '@mescius/wijmo.interop.grid': 'npm:@mescius/wijmo.interop.grid/index.js',
            '@mescius/wijmo.touch': 'npm:@mescius/wijmo.touch/index.js',
            '@mescius/wijmo.cloud': 'npm:@mescius/wijmo.cloud/index.js',
            '@mescius/wijmo.barcode': 'npm:@mescius/wijmo.barcode/index.js',
            '@mescius/wijmo.barcode.common': 'npm:@mescius/wijmo.barcode.common/index.js',
            '@mescius/wijmo.barcode.composite': 'npm:@mescius/wijmo.barcode.composite/index.js',
            '@mescius/wijmo.barcode.specialized': 'npm:@mescius/wijmo.barcode.specialized/index.js',
            "@mescius/wijmo.angular2.chart.analytics": "npm:@mescius/wijmo.angular2.chart.analytics/index.js",
            "@mescius/wijmo.angular2.chart.animation": "npm:@mescius/wijmo.angular2.chart.animation/index.js",
            "@mescius/wijmo.angular2.chart.annotation": "npm:@mescius/wijmo.angular2.chart.annotation/index.js",
            "@mescius/wijmo.angular2.chart.finance.analytics": "npm:@mescius/wijmo.angular2.chart.finance.analytics/index.js",
            "@mescius/wijmo.angular2.chart.finance": "npm:@mescius/wijmo.angular2.chart.finance/index.js",
            "@mescius/wijmo.angular2.chart.hierarchical": "npm:@mescius/wijmo.angular2.chart.hierarchical/index.js",
            "@mescius/wijmo.angular2.chart.interaction": "npm:@mescius/wijmo.angular2.chart.interaction/index.js",
            "@mescius/wijmo.angular2.chart.radar": "npm:@mescius/wijmo.angular2.chart.radar/index.js",
            '@mescius/wijmo.angular2.chart.map': 'npm:@mescius/wijmo.angular2.chart.map/index.js',
            "@mescius/wijmo.angular2.chart": "npm:@mescius/wijmo.angular2.chart/index.js",
            "@mescius/wijmo.angular2.core": "npm:@mescius/wijmo.angular2.core/index.js",
            "@mescius/wijmo.angular2.gauge": "npm:@mescius/wijmo.angular2.gauge/index.js",
            "@mescius/wijmo.angular2.grid.detail": "npm:@mescius/wijmo.angular2.grid.detail/index.js",
            "@mescius/wijmo.angular2.grid.filter": "npm:@mescius/wijmo.angular2.grid.filter/index.js",
            "@mescius/wijmo.angular2.grid.grouppanel": "npm:@mescius/wijmo.angular2.grid.grouppanel/index.js",
            "@mescius/wijmo.angular2.grid.search": "npm:@mescius/wijmo.angular2.grid.search/index.js",
            "@mescius/wijmo.angular2.grid.multirow": "npm:@mescius/wijmo.angular2.grid.multirow/index.js",
            "@mescius/wijmo.angular2.grid.sheet": "npm:@mescius/wijmo.angular2.grid.sheet/index.js",
            '@mescius/wijmo.angular2.grid.transposed': 'npm:@mescius/wijmo.angular2.grid.transposed/index.js',
            '@mescius/wijmo.angular2.grid.transposedmultirow': 'npm:@mescius/wijmo.angular2.grid.transposedmultirow/index.js',
            "@mescius/wijmo.angular2.grid": "npm:@mescius/wijmo.angular2.grid/index.js",
            "@mescius/wijmo.angular2.input": "npm:@mescius/wijmo.angular2.input/index.js",
            "@mescius/wijmo.angular2.olap": "npm:@mescius/wijmo.angular2.olap/index.js",
            "@mescius/wijmo.angular2.viewer": "npm:@mescius/wijmo.angular2.viewer/index.js",
            "@mescius/wijmo.angular2.nav": "npm:@mescius/wijmo.angular2.nav/index.js",
            "@mescius/wijmo.angular2.directivebase": "npm:@mescius/wijmo.angular2.directivebase/index.js",
            '@mescius/wijmo.angular2.barcode.common': 'npm:@mescius/wijmo.angular2.barcode.common/index.js',
            '@mescius/wijmo.angular2.barcode.composite': 'npm:@mescius/wijmo.angular2.barcode.composite/index.js',
            '@mescius/wijmo.angular2.barcode.specialized': 'npm:@mescius/wijmo.angular2.barcode.specialized/index.js',
                    
            'bootstrap.css': 'npm:bootstrap/dist/css/bootstrap.min.css',
            'jszip': 'https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js',
            "@angular/common/http": "https://cdn.jsdelivr.net/npm/@angular/common@16.2.6/fesm2022/http.mjs",
            "@angular/core": "https://cdn.jsdelivr.net/npm/@angular/core@16.2.6/fesm2022/core.mjs",
            "@angular/platform-browser": "https://cdn.jsdelivr.net/npm/@angular/platform-browser@16.2.6/fesm2022/platform-browser.mjs",
            "@angular/common": "https://cdn.jsdelivr.net/npm/@angular/common@16.2.6/fesm2022/common.mjs",
            "@angular/compiler": "https://cdn.jsdelivr.net/npm/@angular/compiler@16.2.6/fesm2022/compiler.mjs",
            "@angular/forms": "https://cdn.jsdelivr.net/npm/@angular/forms@16.2.6/fesm2022/forms.mjs",
            "@angular/localize": "https://cdn.jsdelivr.net/npm/@angular/localize@16.2.6/fesm2022/localize.mjs",
            "@angular/platform-browser-dynamic": "https://cdn.jsdelivr.net/npm/@angular/platform-browser-dynamic@16.2.6/fesm2022/platform-browser-dynamic.mjs",
        },
        // packages tells the System loader how to load when no filename and/or no extension
        packages: {
            "./src": {
                defaultExtension: 'ts'
            },
            "node_modules": {
                defaultExtension: 'js'
            },
            wijmo: {
                defaultExtension: 'js',
            }
        }
    });
})(this);